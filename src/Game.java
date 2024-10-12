import java.awt.Point;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;

import processing.core.PConstants;

public class Game extends PApplet {

    public static final int X_RATIO = 16;
    public static final int Y_RATIO = 9;
    public static final int WINDOW_SIZE = 60;

    private final char[] ELEVATOR_KEYS = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'};
    private final int MAX_ELEVATORS = ELEVATOR_KEYS.length;
    private final int MAX_QUEUE_DISPLAYED = 10;

    private int currentNumFloors;
    private Elevator selectedElevator;
    private ArrayList<Elevator> elevators;
    private HashMap<Character, Elevator> charToElevatorMap;
    
    private ArrayList<Person> peopleInLine;
    private Upgrades upgrades;
    private int points;
    private int credits;

    private Hint hint;
    private boolean buyMenuHintShown;


    public static void main(String[] args) {
        PApplet.main("Game");
    }

    public void settings() {
        size(WINDOW_SIZE * X_RATIO, WINDOW_SIZE * Y_RATIO);
        smooth();
    }

    public void setup() {
        // Window
        windowResizable(true);
        windowTitle("Elevator Simulator");

        // Fonts
        FontHolder.setRegular(createFont("GeistMono-Regular.otf", 128));
        FontHolder.setMedium(createFont("GeistMono-Medium.otf", 128));
        textMode(PConstants.MODEL);

        // Elevators
        currentNumFloors = 3;
        selectedElevator = null;
        elevators = new ArrayList<>();
        charToElevatorMap = new HashMap<>();
        addElevator(500, 50, 400, 200);
        // addElevator(500, 300, 400, 200);

        // People
        peopleInLine = new ArrayList<>();
        loopSpawnNewPeople(3000, 5000);

        // Points / Upgrades
        points = 0;
        credits = 0;
        upgrades = new Upgrades(this);

        // Hints
        hint = null;
        buyMenuHintShown = false;
    }

    public void draw() {
        windowRatio(WINDOW_SIZE * X_RATIO, WINDOW_SIZE * Y_RATIO);

        // Background (clears screen too)
        background(255);

        // Game Title
        strokeWeight(0);
        textFont(FontHolder.getRegular());
        fill(0);
        rect(15, 15, 400, 50);      // outer black rect
        textSize(32);
        fill(255);
        textAlign(PConstants.LEFT, PConstants.CENTER);
        text("Elevator Simulator", 20, 40);
        rect(380, 25, 20, 30);      // small white rectangle symbol

        // Queue
        fill(0);
        textFont(FontHolder.getMedium());
        textSize(24);
        text("Queue", 20, 100);
        textFont(FontHolder.getRegular());
        textSize(19);
        int numPeopleInLine = peopleInLine.size();
        int numPeopleToDisplay, numPeopleNotDisplayed;
        if (numPeopleInLine > MAX_QUEUE_DISPLAYED) {
            numPeopleToDisplay = MAX_QUEUE_DISPLAYED;
            numPeopleNotDisplayed = numPeopleInLine - MAX_QUEUE_DISPLAYED;
        } else {
            numPeopleToDisplay = numPeopleInLine;
            numPeopleNotDisplayed = 0;
        }
        for (int i = 0; i < numPeopleToDisplay; i++) {
            text(peopleInLine.get(i).toString(), 20, 130 + i * 24);
        }
        if (numPeopleNotDisplayed > 0) text(numPeopleNotDisplayed + " more...", 20, 130 + numPeopleToDisplay * 24);

        // Points
        textAlign(PConstants.LEFT, PConstants.CENTER);
        textSize(20);
        text("Credits: " + credits, 20, 440);
        textSize(40);
        text("Points: " + points, 20, 480);
        
        // Draw Elevators
        textFont(FontHolder.getMedium());
        for (Elevator elevator : elevators) {
            elevator.draw(this);
        }

        // Upgrades Menu
        upgrades.draw(this);

        // Hints
        if (hint != null) {
            hint.draw(this);
        }
    }

    public void mousePressed() {
        Point mouse = Game.getScaledMouse(this);

        // If hint was clicked on, remove it
        if (hint != null && hint.contains(mouse.x, mouse.y)) {
            hint = null;
        }

        for (Elevator elevator : elevators) {
            elevator.mousePressed(mouse.x, mouse.y);
        }

        upgrades.mousePressed(mouse.x, mouse.y);
    }

    public void keyPressed() {
        // Make the key lowercase to make it work even if the user did a capital for some reason
        char key = Character.toLowerCase(this.key);

        if (key == 'b') {
            upgrades.toggleMenuDisplay();
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
        credits++;
        points++;

        if (!buyMenuHintShown && credits >= 10) {
            hint = new Hint(500, height + 50, 450, "Press 'b' to open the upgrades menu");
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

    public int getNumFloors() {
        return currentNumFloors;
    }

    public int getCredits() {
        return credits;
    }

    public void spendCredits(int amount) {
        credits -= amount;
    }

    public Point getScaledMouse() {
        return getScaledMouse(this);
    }

    public static Point getScaledMouse(PApplet d) {
        int scaledMouseX = (int)(1.0 * d.mouseX / d.width * WINDOW_SIZE * X_RATIO);
        int scaledMouseY = (int)(1.0 * d.mouseY / d.height * WINDOW_SIZE * Y_RATIO);
        
        return new Point(scaledMouseX, scaledMouseY);
    }

}
