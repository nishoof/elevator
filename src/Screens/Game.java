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

    private class Wave {
    
        private int numPeople;
        private int minDelay;
        private int maxDelay;
    
        /**
         * Constructs a new Wave. A wave will spawn a numPeople amount of people with a delay in the range of [avgDelay - delayRange/2, avgDelay + delayRange/2)
         * @param numPeople the number of people in this wave
         * @param minDelay the minimum delay between spawning people, in miliseconds
         * @param maxDelay the maximum delay between spawning people, in miliseconds
         */
        private Wave(int numPeople, int minDelay, int maxDelay) {
            if (numPeople < 1) throw new IllegalArgumentException("Must have at least 1 person in the wave");
            if (minDelay < 0 || maxDelay < 0) throw new IllegalArgumentException("Delay cannot be negative");
            if (maxDelay < minDelay) throw new IllegalArgumentException("Max delay cannot be less than min delay");

            this.numPeople = numPeople;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
        }

        private void start() {
            for (int i = 0; i < numPeople; i++) {
                // Spawn the person
                newPerson();

                // Delay
                try {
                    Thread.sleep((long)(Math.random() * (maxDelay - minDelay) + minDelay));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    
    }

    private final char[] ELEVATOR_KEYS = {'q', 'w'};
    private final int MAX_ELEVATORS = ELEVATOR_KEYS.length;
    private final int MAX_QUEUE_DISPLAYED = 10;

    private int currentNumFloors;
    private Elevator selectedElevator;
    private ArrayList<Elevator> elevators;
    private HashMap<Character, Elevator> charToElevatorMap;

    private ArrayList<Person> peopleInLine;
    private int[][] waves;
    private long startTime;

    private Hint hint;
    private boolean buyMenuHintShown;

    /**
     * Constructs a new Game
     * @param numElevators the number of elevators in the game
     * @param startingNumFloors the number of floors per elevator the game starts with
     * @param waves the waves of people to spawn. Each wave is an array of 3 integers: [numPeople, minDelay, maxDelay]. numPeople is the number of people in this wave. The people will be spawned with a delay between minDelay (inclusive) and maxDelay (exclusive).
     */
    public Game(int numElevators, int startingNumFloors, int[][] waves) {

        if (numElevators < 1) throw new IllegalArgumentException("Must have at least 1 elevator");
        if (numElevators > ELEVATOR_KEYS.length) throw new IllegalArgumentException("Cannot have more than " + ELEVATOR_KEYS.length + " elevators");

        // Elevators
        this.currentNumFloors = startingNumFloors;
        this.selectedElevator = null;
        this.elevators = new ArrayList<>();
        this.charToElevatorMap = new HashMap<>();
        int elevatorY = 50;
        for (int i = 0; i < numElevators; i++) {
            addElevator(500, elevatorY, 400, 200);
            elevatorY += 250;
        }

        // People
        this.peopleInLine = new ArrayList<>();
        this.waves = waves;

        // Time
        startTime = 0;

        // Hints
        hint = null;
        buyMenuHintShown = false;
    }

    @Override
    public void draw(PApplet d) {
        if (startTime == 0) throw new IllegalStateException("Tried to draw the game screen before starting the game");

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

        if (key >= 'a' && key <= 'z') {                   // keyIsALetter
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
            newPerson();
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

    private void newPerson() {
        int minFloor = 1;
        int maxFloor = currentNumFloors;

        int currentFloor, desiredFloor;
        
        currentFloor = (int)(Math.random() * (maxFloor - minFloor + 1) + minFloor);
        
        do {
            desiredFloor = (int)(Math.random() * (maxFloor - minFloor + 1) + minFloor);
        } while (desiredFloor == currentFloor);

        Person person = new Person(currentFloor, desiredFloor);
        
        peopleInLine.add(person);
    }

    private void loopSpawnNewPeople() {
        new Thread(() -> {
            // while (true) {
            //     newPerson(1, currentNumFloors);
            //     try {
            //         Thread.sleep((long)(Math.random() * (maxDelay - minDelay) + minDelay));
            //     } catch (InterruptedException e) {
            //         e.printStackTrace();
            //     }
            // }

            for (int[] wave : waves) {
                new Wave(wave[0], wave[1], wave[2]).start();
            }
        }).start();
    }

    private void addElevator(int x, int y, int width, int height) {
        // Get the number of elevators that are currently in the game
        int numElevators = elevators.size();

        // Make sure we aren't exceeding the maximum number of elevators
        if (numElevators >= MAX_ELEVATORS) throw new IllegalStateException("Cannot add more elevators");

        // Create the elevator and add it to the list of elevators and the map
        Elevator elevator = new Elevator(x, y, width, height, currentNumFloors, this);       // TODO: Uncomment this line
        // Elevator elevator = new Elevator(x, y, width, height, 10, this);
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

    public void startTime() {
        startTime = System.currentTimeMillis();
        loopSpawnNewPeople();
        // loopSpawnNewPeople(2000, 3500);
    }

}
