package Screens;

import java.util.ArrayList;
import java.util.HashMap;

import Elements.Elevator;
import Elements.Hint;
import Elements.Person;
import Main.FontHolder;
import Main.Main;
import Main.PlayerStats;

import processing.core.PApplet;
import processing.core.PConstants;

public class Game implements Screen {

    private final char[] ELEVATOR_KEYS = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'};
    private final int MAX_ELEVATORS = ELEVATOR_KEYS.length;
    private final int MAX_QUEUE_DISPLAYED = 10;

    private int currentNumFloors;
    private Elevator selectedElevator;
    private ArrayList<Elevator> elevators;
    private HashMap<Character, Elevator> charToElevatorMap;
    
    private ArrayList<Person> peopleInLine;
    private long startTime;

    private Hint hint;
    private boolean buyMenuHintShown;

    public Game() {
        // Elevators
        currentNumFloors = 3;
        selectedElevator = null;
        elevators = new ArrayList<>();
        charToElevatorMap = new HashMap<>();
        addElevator(500, 50, 400, 200);
        // addElevator(500, 300, 400, 200);

        // People
        peopleInLine = new ArrayList<>();
        loopSpawnNewPeople(2000, 3500);

        // Time
        startTime = System.currentTimeMillis();

        // Hints
        hint = null;
        buyMenuHintShown = false;
    }

    @Override
    public void draw(PApplet d) {
        // Game Title
        d.strokeWeight(0);
        d.textFont(FontHolder.getRegular());
        d.fill(0);
        d.rect(15, 15, 400, 50);      // outer black rect
        d.textSize(32);
        d.fill(255);
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.text("Elevator Simulator", 20, 40);
        d.rect(380, 25, 20, 30);      // small white rectangle symbol

        // Queue
        d.fill(0);
        d.textFont(FontHolder.getMedium());
        d.textSize(24);
        d.text("Queue", 20, 100);
        d.textFont(FontHolder.getRegular());
        d.textSize(19);
        int numPeopleInLine = peopleInLine.size();
        int numPeopleToDisplay = Math.min(numPeopleInLine, MAX_QUEUE_DISPLAYED);
        int numPeopleNotDisplayed = numPeopleInLine - numPeopleToDisplay;
        for (int i = 0; i < numPeopleToDisplay; i++) {
            d.text(peopleInLine.get(i).toString(), 20, 130 + i * 24);
        }
        if (numPeopleNotDisplayed > 0) d.text(numPeopleNotDisplayed + " more...", 20, 130 + numPeopleToDisplay * 24);

        // Points
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.textSize(20);
        d.text("Credits: " + PlayerStats.getCredits(), 20, 420);
        d.textSize(40);
        d.text("Points: " + PlayerStats.getPoints(), 20, 460);

        // Stopwatch
        long elapsedTime = System.currentTimeMillis() - startTime;
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = elapsedTime / (1000 * 60);
        d.textSize(20);
        d.text(String.format("Time: %02d:%02d", minutes, seconds), 20, 500);
        
        // Draw Elevators
        d.textFont(FontHolder.getMedium());
        for (Elevator elevator : elevators) {
            elevator.draw(d);
        }

        // Hints
        if (hint != null) {
            hint.draw(d);
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        // If hint was clicked on, remove it
        if (hint != null && hint.contains(mouseX, mouseY)) {
            hint = null;
        }

        for (Elevator elevator : elevators) {
            elevator.mousePressed(mouseX, mouseY);
        }
    }

    @Override
    public void keyPressed(char key) {
        // Make the key lowercase to make it work even if the user did a capital for some reason
        key = Character.toLowerCase(key);

        if (key == 'b') {
            Main.getInstance().switchScreen(Main.UPGRADES);
        } else if (key >= 'a' && key <= 'z') {                   // keyIsALetter
            // Figure out what elevator is paired to the key that was pressed
            Elevator elevator = charToElevatorMap.get(key);

            // Ignore keys that aren't paired to an elevator
            if (elevator == null) return;

            // Select the elevator
            if (selectedElevator == elevator) {
                selectedElevator.setHighlighted(false);
                selectedElevator = null;
            } else {
                if (selectedElevator != null) selectedElevator.setHighlighted(false);
                selectedElevator = elevator;
                selectedElevator.setHighlighted(true);
            }
        } else if (key >= '1' && key <= '9') {            // keyIsANumber
            // If there is no elevator selected already, then there's nothing we can do
            if (selectedElevator == null) return;

            // Otherwise, tell the elevator to go to this floor
            selectedElevator.addFloorToQueue(key - '0');
        } else if (key == '`') {
            newPerson(1, currentNumFloors);
        } else if (key == '+') {
            increaseFloorCount();
        }
    }

    public ArrayList<Person> getPeopleInLine() {
        return peopleInLine;
    }

    public void rewardPoints() {
        PlayerStats.addCreditsAndPoints();

        if (!buyMenuHintShown && PlayerStats.getCredits() >= 10) {
            hint = new Hint(500, Main.WINDOW_HEIGHT + 50, Main.WINDOW_HEIGHT - 90, "Press 'b' to open the upgrades menu");
            buyMenuHintShown = true;
        }
    }

    private void newPerson(int minFloor, int maxFloor) {
        int currentFloor, desiredFloor;
        
        currentFloor = (int)(Math.random() * (maxFloor - minFloor + 1) + minFloor);
        
        do {
            desiredFloor = (int)(Math.random() * (maxFloor - minFloor + 1) + minFloor);
        } while (desiredFloor == currentFloor);

        Person person = new Person(currentFloor, desiredFloor);
        
        peopleInLine.add(person);
    }

    private void loopSpawnNewPeople(int minDelay, int maxDelay) {
        new Thread(() -> {
            while (true) {
                newPerson(1, currentNumFloors);
                try {
                    Thread.sleep((long)(Math.random() * (maxDelay - minDelay) + minDelay));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void addElevator(int x, int y, int width, int height) {
        // Get the number of elevators that are currently in the game
        int numElevators = elevators.size();

        // Make sure we aren't exceeding the maximum number of elevators
        if (numElevators >= MAX_ELEVATORS) throw new IllegalStateException("Cannot add more elevators");

        // Create the elevator and add it to the list of elevators and the map
        Elevator elevator = new Elevator(x, y, width, height, currentNumFloors, this);
        elevators.add(elevator);
        charToElevatorMap.put(ELEVATOR_KEYS[numElevators], elevator);
    }

    public void increaseFloorCount() {
        try {
            for (Elevator elevator : elevators) {
                elevator.addFloor();
            }
        } catch (IllegalStateException e) {
            System.err.println("Unable to add more floors");
            e.printStackTrace();
            return;
        }
        currentNumFloors++;
    }

    public void upgradeCapacity() {
        for (Elevator elevator : elevators) {
            elevator.upgradeCapacity();
        }
    }

    public int getNumFloors() {
        return currentNumFloors;
    }

    public void startTime() {
        startTime = System.currentTimeMillis();
    }

}
