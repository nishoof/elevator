package Main;

import java.util.ArrayList;

public class PlayerStats {

    public static final int CAPACITY_UPGRADE_COST = 15;
    public static final int MOVEMENT_SPEED_UPGRADE_COST = 10;
    public static final int DOOR_SPEED_UPGRADE_COST = 14;
    public static final int PEOPLE_SPEED_UPGRADE_COST = 20;

    private static final ArrayList<UpgradeEventListener> upgradeListeners = new ArrayList<>();;

    private static int credits = 50;
    private static int points = 0;

    private static int capacity = 3;
    private static int movementSpeed = 1;
    private static int doorSpeed = 1;
    private static int peopleSpeed = 1;

	public static int getCredits() {
		return credits;
	}

    public static int getPoints() {
        return points;
    }

    public static int getCapacity() {
        return capacity;
    }

    public static int getMovementSpeed() {
        return movementSpeed;
    }
    
    public static double getSecPerFloor() {
        return 1.0 / (movementSpeed * 0.1 + 1.5);
    }
    
    /**
     * Returns the time in seconds for the delay between elevator stop -> door open or door open -> elevator move
     */
    public static double getSecDoorsDelay() {
        return 0.85 / (movementSpeed * 0.1 + 1.5);
    }

    public static int getDoorSpeed() {
        return doorSpeed;
    }

    /**
     * Returns the time in seconds it takes for the door to open/close (animation time)
     */
    public static double getSecDoorsToOpen() {
        return 0.25 * (1.0 / doorSpeed);
    }

    public static int getPeopleSpeed() {
        return peopleSpeed;
    }

    /**
     * Returns the time in seconds for how long the door stays open for
     */
    public static double getSecDoorsOpen() {
        return 1.5 * (1.0 / peopleSpeed);
    }
    
	public static void addCreditsAndPoints() {
        credits++;
        points++;
    }

    /**
     * Spends the given amount of credits. Returns true if the credits were spent successfully, false otherwise (not enough credits to spend).
     */
    public static boolean spendCredits(int amount) {
        if (amount > credits) return false;
        credits -= amount;
        return true;
    }

    public static boolean upgradeCapacity() {
        boolean enoughCredits = spendCredits(CAPACITY_UPGRADE_COST);
        if (!enoughCredits) return false;
        capacity++;
        notifyUpgradeEventListeners();
        return true;
    }

    public static boolean upgradeMovementSpeed() {
        boolean enoughCredits = spendCredits(MOVEMENT_SPEED_UPGRADE_COST);
        if (!enoughCredits) return false;
        movementSpeed += 1;
        notifyUpgradeEventListeners();
        return true;
    }

    public static boolean upgradeDoorSpeed() {
        boolean enoughCredits = spendCredits(DOOR_SPEED_UPGRADE_COST);
        if (!enoughCredits) return false;
        doorSpeed += 1;
        notifyUpgradeEventListeners();
        return true;
    }

    public static boolean upgradePeopleSpeed() {
        boolean enoughCredits = spendCredits(PEOPLE_SPEED_UPGRADE_COST);
        if (!enoughCredits) return false;
        peopleSpeed += 1;
        notifyUpgradeEventListeners();
        return true;
    }

    public static void addUpgradeEventListener(UpgradeEventListener listener) {
        upgradeListeners.add(listener);
    }

    private static void notifyUpgradeEventListeners() {
        for (UpgradeEventListener listener : upgradeListeners) {
            listener.onUpgrade();
        }
    }

    public static String getAllUpgradeStats() {
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
}
