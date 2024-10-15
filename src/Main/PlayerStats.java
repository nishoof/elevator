package Main;

import java.util.ArrayList;

public class PlayerStats {

    public static final int ELEVATOR_CAPACITY_UPGRADE_COST = 10;

    private static final ArrayList<UpgradeEventListener> upgradeListeners = new ArrayList<>();;

    private static int credits = 0;
    private static int points = 0;

    private static int elevatorCapacity = 3;

	public static int getCredits() {
		return credits;
	}

    public static int getPoints() {
        return points;
    }

    public static int getElevatorCapacity() {
        return elevatorCapacity;
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
        boolean enoughCredits = spendCredits(ELEVATOR_CAPACITY_UPGRADE_COST);
        if (!enoughCredits) return false;
        elevatorCapacity++;
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

}
