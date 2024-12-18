package Screens;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

import Elements.Elevator;
import Elements.Hint;
import Elements.Person;
import Elements.Button.Button;
import Elements.UpgradePanel;
import Main.DataHolder;
import Main.Main;
import Main.PlayerStats;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class Game implements Screen {

    private class Wave {

        private int numPeople;
        private int minDelay;
        private int maxDelay;

        /**
         * Constructs a new Wave. A wave will spawn a numPeople amount of people with a
         * delay in the range of [avgDelay - delayRange/2, avgDelay + delayRange/2)
         *
         * @param numPeople the number of people in this wave
         * @param minDelay  the minimum delay between spawning people, in miliseconds
         * @param maxDelay  the maximum delay between spawning people, in miliseconds
         */
        private Wave(int numPeople, int minDelay, int maxDelay) {
            if (numPeople < 1)
                throw new IllegalArgumentException("Must have at least 1 person in the wave");
            if (minDelay < 0 || maxDelay < 0)
                throw new IllegalArgumentException("Delay cannot be negative");
            if (maxDelay < minDelay)
                throw new IllegalArgumentException("Max delay cannot be less than min delay");

            this.numPeople = numPeople;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
        }

        private void start() {
            for (int i = 0; i < numPeople; i++) {
                // Spawn the person
                newPerson();

                // Delay
                if (i == numPeople - 1)
                    continue; // don't delay on last person
                try {
                    Thread.sleep((long) (Math.random() * (maxDelay - minDelay) + minDelay));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private final char[] ELEVATOR_KEYS = { 'q', 'w' };
    private final int MAX_ELEVATORS = ELEVATOR_KEYS.length;
    private final int MAX_QUEUE_DISPLAYED = 10;

    private int currentNumFloors;
    private Elevator selectedElevator;
    private ArrayList<Elevator> elevators;
    private HashMap<Character, Elevator> charToElevatorMap;

    private List<Person> peopleInLine; // synchronized list, use a synchronized block
    private int[][] waves;
    private long startTime;
    private long endTime;
    private boolean doneSpawningPeople;

    private PlayerStats playerStats;
    private UpgradePanel upgradePanel;

    private Hint hint;
    private boolean buyMenuHintShown;

    private PImage heartImg;
    private int lives;
    private boolean gameOver;
    private boolean won;
    private boolean lastGameFrameDrawn; // we draw one last frame after the game is over
    private Button returnToMenuButton;

    /**
     * Constructs a new Game
     *
     * @param numElevators      the number of elevators in the game
     * @param startingNumFloors the number of floors per elevator the game starts
     *                          with
     * @param waves             the waves of people to spawn. Each wave is an array
     *                          of 3 integers: [numPeople, minDelay, maxDelay].
     *                          numPeople is the number of people in this wave. The
     *                          people will be spawned with a delay between minDelay
     *                          (inclusive) and maxDelay (exclusive).
     */
    public Game(int numElevators, int startingNumFloors, int[][] waves) {
        if (numElevators < 1)
            throw new IllegalArgumentException("Must have at least 1 elevator");
        if (numElevators > ELEVATOR_KEYS.length)
            throw new IllegalArgumentException("Cannot have more than " + ELEVATOR_KEYS.length + " elevators");

        // Player Stats
        playerStats = new PlayerStats();

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
        this.peopleInLine = Collections.synchronizedList(new ArrayList<>());
        this.waves = waves;
        doneSpawningPeople = false;

        // Time
        startTime = 0;

        // Upgrade Panel
        upgradePanel = new UpgradePanel(620, 40, 300, 460, playerStats);

        // Hints
        hint = null;
        buyMenuHintShown = false;

        // Lives
        heartImg = DataHolder.getHeartImg();
        lives = 3;
        gameOver = false;
        won = false;
        lastGameFrameDrawn = false;
        returnToMenuButton = Main.getInstance().getReturnToMenuButton();
    }

    @Override
    public void draw(PApplet d) {
        if (startTime == 0)
            throw new IllegalStateException("Tried to draw the game screen before starting the game");

        // Draw game over screen
        if (gameOver) {
            drawGameOverScreen(d);
        }

        // If the game is over, don't draw any new things in the background
        if (lastGameFrameDrawn) {
            return;
        }

        // Check if the game was won
        if (doneSpawningPeople && peopleInLine.size() == 0) {
            boolean elevatorsEmpty = true;
            for (Elevator elevator : elevators) {
                if (elevator.getPeopleInElevator().size() != 0)
                    elevatorsEmpty = false;
            }
            if (elevatorsEmpty)
                gameOver(true);
        }

        d.background(255);

        // Game Title
        d.strokeWeight(0);
        d.textFont(DataHolder.getRegularFont());
        d.fill(0);
        d.rect(15, 15, 400, 50); // outer black rect
        d.textSize(32);
        d.fill(255);
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.text("Elevator Simulator", 20, 40);
        d.rect(380, 25, 20, 30); // small white rectangle symbol

        // Queue
        d.fill(0);
        d.textFont(DataHolder.getMediumFont());
        d.textSize(24);
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.text("Queue", 20, 100);
        d.textFont(DataHolder.getRegularFont());
        d.textSize(19);
        int numPeopleInLine = peopleInLine.size();
        int numPeopleToDisplay = Math.min(numPeopleInLine, MAX_QUEUE_DISPLAYED);
        int numPeopleNotDisplayed = numPeopleInLine - numPeopleToDisplay;
        synchronized (peopleInLine) {
            for (int i = 0; i < numPeopleToDisplay; i++) {
                peopleInLine.get(i).draw(d, 20, 130 + i * 24);
            }
        }
        if (numPeopleNotDisplayed > 0)
            d.text(numPeopleNotDisplayed + " more...", 20, 130 + numPeopleToDisplay * 24);

        // Points
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.textSize(20);
        d.text("Credits: " + playerStats.getCredits(), 20, 420);
        d.textSize(40);
        d.text("Points: " + playerStats.getPoints(), 20, 460);

        // Stopwatch
        long elapsedTime = System.currentTimeMillis() - startTime;
        String time = getTimeStr(elapsedTime, false);
        d.textSize(20);
        d.text(time, 20, 500);

        // Draw Elevators
        d.textFont(DataHolder.getMediumFont());
        for (Elevator elevator : elevators) {
            elevator.draw(d);
        }

        // Hints
        if (hint != null) {
            hint.draw(d);
        }

        // Lives
        for (int i = 0; i < lives; i++) {
            d.image(heartImg, Main.WINDOW_WIDTH - 80 - i * 60, Main.WINDOW_HEIGHT - 80);
        }

        // Upgrade Panel
        upgradePanel.draw(d);

        // Last Game Frame Drawn
        // Basically, once the game is over, we draw one last frame of the game, then
        // draw the game over screen
        // This is so that the player can see the last state of the game before it ends
        // (with 0 hearts and the person leaving the queue)
        if (gameOver) {
            lastGameFrameDrawn = true;
            d.rectMode(PConstants.CORNER);
            d.fill(255, 150);
            d.rect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        }
    }

    public void drawGameOverScreen(PApplet d) {
        d.textFont(DataHolder.getRegularFont());
        d.textSize(64);
        d.textAlign(PConstants.CENTER, PConstants.CENTER);

        if (won) {
            d.fill(0, 255, 0);
            d.text("Level Complete!", Main.WINDOW_WIDTH / 2, Main.WINDOW_HEIGHT / 2 - 70);
        } else {
            d.fill(255, 0, 0);
            d.text("Game Over", Main.WINDOW_WIDTH / 2, Main.WINDOW_HEIGHT / 2 - 70);
        }

        String time = getTimeStr(endTime, true);
        d.fill(0);
        d.textSize(30);
        d.text(time, Main.WINDOW_WIDTH / 2, Main.WINDOW_HEIGHT / 2 - 20);

        returnToMenuButton.draw(d);
    }

    private String getTimeStr(long elapsedTime, boolean displayMilliseconds) {
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = elapsedTime / (1000 * 60);

        String str = String.format("Time: %02d:%02d", minutes, seconds);

        if (displayMilliseconds) {
            str += String.format(".%03d", elapsedTime % 1000);
        }

        return str;
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        if (gameOver) {
            returnToMenuButton.mousePressed(mouseX, mouseY);
            return;
        }

        // Upgrade Panel
        boolean upgradePanelClicked = upgradePanel.mousePressed(mouseX, mouseY);
        if (upgradePanelClicked)
            return;

        // If hint was clicked on, remove it
        if (hint != null && hint.contains(mouseX, mouseY)) {
            hint = null;
            return;
        }

        for (Elevator elevator : elevators) {
            elevator.mousePressed(mouseX, mouseY);
        }
    }

    @Override
    public void keyPressed(char key) {
        if (gameOver)
            return;

        // Make the key lowercase to make it work even if the user did a capital for
        // some reason
        key = Character.toLowerCase(key);

        if (key >= 'a' && key <= 'z') { // keyIsALetter
            if (key == 'b') {
                upgradePanel.toggleVisible();
            } else {
                selectElevator(key);
            }
        } else if (key >= '1' && key <= '9') { // keyIsANumber
            // If there is no elevator selected already, then there's nothing we can do
            if (selectedElevator == null)
                return;

            // Otherwise, tell the elevator to go to this floor
            selectedElevator.addFloorToQueue(key - '0');
        } else if (key == '`') {
            newPerson();
        } else if (key == '+') {
            increaseFloorCount();
        }
    }

    private void selectElevator(char key) {
        // Figure out what elevator is paired to the key that was pressed
        Elevator elevator = charToElevatorMap.get(key);

        // Ignore keys that aren't paired to an elevator
        if (elevator == null)
            return;

        // Select the elevator
        if (selectedElevator == elevator) {
            selectedElevator.setHighlighted(false);
            selectedElevator = null;
        } else {
            if (selectedElevator != null)
                selectedElevator.setHighlighted(false);
            selectedElevator = elevator;
            selectedElevator.setHighlighted(true);
        }
    }

    public void rewardPoints() {
        if (gameOver)
            return;

        playerStats.addCreditsAndPoints();

        if (!buyMenuHintShown && playerStats.getCredits() >= 10) {
            hint = new Hint(500, Main.WINDOW_HEIGHT + 50, Main.WINDOW_HEIGHT - 90,
                    "Press 'b' to open the upgrades menu");
            buyMenuHintShown = true;
        }
    }

    private void newPerson() {
        int minFloor = 1;
        int maxFloor = currentNumFloors;

        int currentFloor, desiredFloor;

        currentFloor = (int) (Math.random() * (maxFloor - minFloor + 1) + minFloor);

        do {
            desiredFloor = (int) (Math.random() * (maxFloor - minFloor + 1) + minFloor);
        } while (desiredFloor == currentFloor);

        Person person = new Person(currentFloor, desiredFloor, 20_000, this::onPersonTimeOver);

        peopleInLine.add(person);
    }

    // Called when a person has waited too long. Removes the person from the queue
    // and decrements the player's lives
    private void onPersonTimeOver(Person person) {
        if (gameOver)
            return;

        System.out.println("Person " + person + " waited too long");
        peopleInLine.remove(person);

        lives--;
        if (lives <= 0) {
            gameOver(false);
        }
    }

    // Ends the game. Called when the player wins or loses.
    private void gameOver(boolean won) {
        System.out.println("Game Over");
        endTime = System.currentTimeMillis() - startTime;
        this.won = won;
        gameOver = true;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    private void loopSpawnNewPeople() {
        new Thread(() -> {
            for (int[] wave : waves) {
                new Wave(wave[0], wave[1], wave[2]).start();
            }
            doneSpawningPeople = true;
        }).start();
    }

    private void addElevator(int x, int y, int width, int height) {
        // Get the number of elevators that are currently in the game
        int numElevators = elevators.size();

        // Make sure we aren't exceeding the maximum number of elevators
        if (numElevators >= MAX_ELEVATORS)
            throw new IllegalStateException("Cannot add more elevators");

        // Create the elevator and add it to the list of elevators and the map
        Elevator elevator = new Elevator(x, y, width, height, currentNumFloors, this, playerStats);
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
    }

    /**
     * Transfers people from the line to the provided list if they are on the
     * provided floor
     *
     * @param list      the list to transfer the people to
     * @param floor     the floor to check for
     * @param maxPeople the maximum number of people to transfer
     */
    public void transferPeopleFromLine(List<Person> list, int floor, int maxPeople) {
        if (list == null)
            throw new IllegalArgumentException("List cannot be null");
        if (maxPeople < 0)
            throw new IllegalArgumentException("Max people cannot be negative");

        if (maxPeople == 0)
            return;

        synchronized (peopleInLine) {
            int n = 0; // number of people transferred

            for (int i = 0; i < peopleInLine.size(); i++) {
                Person person = peopleInLine.get(i);

                if (person.getCurrentFloor() != floor)
                    continue;

                person.cancelTimer();
                peopleInLine.remove(i);
                list.add(person);

                i--;
                n++;

                if (n == maxPeople)
                    break;
            }
        }
    }

}
