package Main;

public class PlayerStats {
    
    private static int credits = 0;
    private static int points = 0;

	public static int getCredits() {
		return credits;
	}

    public static int getPoints() {
        return points;
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

}
