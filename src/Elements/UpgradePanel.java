package Elements;

import Elements.Button.UpgradeButton;
import Main.PlayerStats;
import processing.core.PApplet;
import processing.core.PConstants;

public class UpgradePanel {

    private final int MARGIN = 20;
    private final int TITLE_TEXT_SIZE = 30;
    private final int TITLE_TEXT_HEIGHT = 26;           // calculate using ascent + descent

    private int x;
    private int y;
    private int width;
    private int height;

    private UpgradeButton[] upgradeButtons;

    private boolean visible;

    public UpgradePanel(int x, int y, int width, int height, PlayerStats playerStats) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        int buttonHeight = (height - TITLE_TEXT_HEIGHT - MARGIN*2) / 4 - MARGIN;
        upgradeButtons = new UpgradeButton[4];
        upgradeButtons[0] = new UpgradeButton(x + MARGIN, y + TITLE_TEXT_HEIGHT + MARGIN*2 + (buttonHeight + MARGIN)*0, width - MARGIN*2, buttonHeight,
            "Capacity", playerStats::getCapacity, playerStats::upgradeCapacity, playerStats.CAPACITY_UPGRADE_COST);
        upgradeButtons[1] = new UpgradeButton(x + MARGIN, y + TITLE_TEXT_HEIGHT + MARGIN*2 + (buttonHeight + MARGIN)*1, width - MARGIN*2, buttonHeight,
            "Movement Speed", playerStats::getMovementSpeed, playerStats::upgradeMovementSpeed, playerStats.MOVEMENT_SPEED_UPGRADE_COST);
        upgradeButtons[2] = new UpgradeButton(x + MARGIN, y + TITLE_TEXT_HEIGHT + MARGIN*2 + (buttonHeight + MARGIN)*2, width - MARGIN*2, buttonHeight,
            "Door Speed", playerStats::getDoorSpeed, playerStats::upgradeDoorSpeed, playerStats.DOOR_SPEED_UPGRADE_COST);
        upgradeButtons[3] = new UpgradeButton(x + MARGIN, y + TITLE_TEXT_HEIGHT + MARGIN*2 + (buttonHeight + MARGIN)*3, width - MARGIN*2, buttonHeight,
            "People Speed", playerStats::getPeopleSpeed, playerStats::upgradePeopleSpeed, playerStats.PEOPLE_SPEED_UPGRADE_COST);
    }

    public void draw(PApplet d) {
        if (!visible) return;

        d.push();          // Save original settings

        // Draw Rect
        d.fill(250);
        d.stroke(0);
        d.strokeWeight(3);
        d.rect(x, y, width, height, 8);

        // Draw Upgrades Title
        d.fill(0);
        d.textSize(TITLE_TEXT_SIZE);
        d.textAlign(PConstants.CENTER, PConstants.TOP);
        d.text("Upgrades", x + width/2, y + 20);

        // Draw Upgrade Buttons
        for (UpgradeButton b : upgradeButtons) {
            b.draw(d);
        }

        d.pop();           // Restore original settings
    }

    public void mousePressed(int x, int y) {
        if (!visible) return;

        for (UpgradeButton b : upgradeButtons) {
            b.mousePressed(x, y);
        }
    }

    public void toggleVisible() {
        visible = !visible;
    }

}
