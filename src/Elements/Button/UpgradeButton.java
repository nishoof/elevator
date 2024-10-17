package Elements.Button;

import processing.core.PApplet;
import processing.core.PConstants;

public class UpgradeButton extends Button {

    // Interface for getting a player stat
    // Used in the UpgradeButton class for a lambda expression
    public interface StatGetter {
        int getStat();
    }

    private final int LEFT_MARGIN = 7;
    private final int TOP_MARGIN = 10;

    private String upgradeName;
    private StatGetter statGetter;
    private int upgradeCost;

    /**
     * Constructs a new UpgradeButton. This button is used to display an upgrade and its cost.
     * Example usage: new UpgradeButton(15, 225, 300, 128, "Test Upgrade", PlayerStats::getCapacity);
     *                new UpgradeButton(15, 225, 300, 128, "Test Upgrade", () -> PlayerStats.getCapacity());
     * @param x x-coordinate of the top-left corner of this Button
     * @param y y-coordinate of the top-left corner of this Button
     * @param width width of this Button
     * @param height height of this Button
     * @param upgradeName the name of the upgrade (displayed on the button)
     * @param statGetter a lambda expression that returns the player stat that this upgrade affects. Must be a method that returns an int.
     */
	public UpgradeButton(int x, int y, int width, int height, String upgradeName, StatGetter statGetter, int upgradeCost) {
		super(x, y, width, height);

        this.upgradeName = upgradeName;
        this.statGetter = statGetter;
        this.upgradeCost = upgradeCost;

        setCornerRounding(0);
	}

    @Override
    public void draw(PApplet d) {
        d.push();          // Save original settings

        super.draw(d);

        int headerSize = getHeight() / 5;
        int normalTextSize = getHeight() / 6;
        int currStat = statGetter.getStat();
        
        int currY = getY() + TOP_MARGIN;

        // Upgrade Name
        d.fill(0);
        d.textSize(headerSize);
        d.textAlign(PConstants.LEFT, PConstants.TOP);
        d.text(upgradeName, getX() + LEFT_MARGIN, currY);
        currY += headerSize + 8;
        
        // Current
        d.textSize(normalTextSize);
        d.text("Current: " + currStat, getX() + LEFT_MARGIN, currY);
        currY += normalTextSize + 5;
        
        // Upgrade To
        d.text("Upgrade To: " + (currStat+1), getX() + LEFT_MARGIN, currY);
        currY += normalTextSize + 5;
        
        // Cost
        d.text("Cost: " + upgradeCost, getX() + LEFT_MARGIN, currY);

        d.pop();           // Restore original settings
    }

    @Override
    public void mousePressed(int x, int y) {
        if (contains(x, y)) {
            System.out.println(statGetter.getStat());
        }
    }

    @Override
    public void setText(String text) {
        throw new UnsupportedOperationException("Cannot set text on an UpgradeButton");
    }

    @Override
    public void setTextSize(int textSize) {
        throw new UnsupportedOperationException("Cannot set textSize on an UpgradeButton");
    }
    
}
