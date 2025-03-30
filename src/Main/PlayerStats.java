package Main;

import java.util.ArrayList;

public class PlayerStats {

    public final int CAPACITY_UPGRADE_COST = 15;
    public final int MOVEMENT_SPEED_UPGRADE_COST = 10;
    public final int DOOR_SPEED_UPGRADE_COST = 14;
    public final int PEOPLE_SPEED_UPGRADE_COST = 20;

    private final int MAX_STAT = 10;

    private ArrayList<UpgradeEventListener> upgradeListeners = new ArrayList<>();;

    private int credits = 50;
    private int points = 0;

    private int capacity = 3;
    private int movementSpeed = 1;
    private int doorSpeed = 1;
    private int peopleSpeed = 1;

    public int getCredits() {
        return credits;
    }

    public int getPoints() {
        return points;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public double getSecPerFloor() {
        return 0.6 - (0.035 * movementSpeed);
    }

    /**
     * Returns the time in seconds for the delay between elevator stop -> door open
     * or door open -> elevator move
     */
    public double getSecDoorsDelay() {
        return 0.5 - (0.045 * movementSpeed);
    }

    public int getDoorSpeed() {
        return doorSpeed;
    }

    /**
     * Returns the time in seconds it takes for the door to open/close (animation
     * time)
     */
    public double getSecDoorsToOpen() {
        return 0.25 - (0.02 * doorSpeed);
    }

    public int getPeopleSpeed() {
        return peopleSpeed;
    }

    /**
     * Returns the time in seconds for how long the door stays open for
     */
    public double getSecDoorsOpen() {
        return 1.5 - (0.125 * peopleSpeed);
    }

    public void addCreditsAndPoints() {
        credits++;
        points++;
    }

    public boolean upgradeCapacity() {
        if (capacity >= MAX_STAT)
            return false;
        boolean enoughCredits = spendCredits(CAPACITY_UPGRADE_COST);
        if (!enoughCredits)
            return false;
        capacity++;
        notifyUpgradeEventListeners();
        return true;
    }

    public boolean upgradeMovementSpeed() {
        if (movementSpeed >= MAX_STAT)
            return false;
        boolean enoughCredits = spendCredits(MOVEMENT_SPEED_UPGRADE_COST);
        if (!enoughCredits)
            return false;
        movementSpeed++;
        notifyUpgradeEventListeners();
        return true;
    }

    public boolean upgradeDoorSpeed() {
        if (doorSpeed >= MAX_STAT)
            return false;
        boolean enoughCredits = spendCredits(DOOR_SPEED_UPGRADE_COST);
        if (!enoughCredits)
            return false;
        doorSpeed++;
        notifyUpgradeEventListeners();
        return true;
    }

    public boolean upgradePeopleSpeed() {
        if (peopleSpeed >= MAX_STAT)
            return false;
        boolean enoughCredits = spendCredits(PEOPLE_SPEED_UPGRADE_COST);
        if (!enoughCredits)
            return false;
        peopleSpeed++;
        notifyUpgradeEventListeners();
        return true;
    }

    public String getAllUpgradeStats() {
        return "Upgrade Stats: "
                + "\n  " + "Elevator capacity: " + getCapacity()
                + "\n  " + "Movement speed: " + getMovementSpeed()
                + "\n  " + "Sec per floor: " + getSecPerFloor()
                + "\n  " + "Sec doors delay: " + getSecDoorsDelay()
                + "\n  " + "Door speed: " + getDoorSpeed()
                + "\n  " + "Sec doors to open: " + getSecDoorsToOpen()
                + "\n  " + "People speed: " + getPeopleSpeed()
                + "\n  " + "Sec doors open: " + getSecDoorsOpen();
    }

    public void addUpgradeEventListener(UpgradeEventListener listener) {
        upgradeListeners.add(listener);
    }

    /**
     * Spends the given amount of credits. Returns true if the credits were spent
     * successfully, false otherwise (not enough credits to spend).
     */
    private boolean spendCredits(int amount) {
        if (amount > credits)
            return false;
        credits -= amount;
        return true;
    }

    private void notifyUpgradeEventListeners() {
        for (UpgradeEventListener listener : upgradeListeners) {
            listener.onUpgrade();
        }
    }

}
