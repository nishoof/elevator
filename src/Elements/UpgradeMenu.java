package Elements;

import Elements.Button.Button;
import Elements.Button.Button.ButtonListener;
import Main.Main;
import Main.PlayerStats;
import processing.core.PApplet;
import processing.core.PConstants;

public class UpgradeMenu {

    private final int MARGIN = 20;
    private final int TITLE_TEXT_SIZE = 30;
    private final int TITLE_TEXT_HEIGHT = 26; // calculate using ascent + descent

    private int visibleX; // x-coordinate when fully visible
    private int hiddenX; // x-coordinate when fully hidden
    private int y;
    private int width;
    private int height;

    private boolean visible;
    private int currX;
    private int targetX;

    private UpgradeUI[] upgradeUIs;
    private int upgradeUIHeight;

    public UpgradeMenu(int x, int y, int width, int height, PlayerStats playerStats) {
        this.visibleX = x;
        this.hiddenX = Main.WINDOW_WIDTH + 10;
        this.y = y;
        this.width = width;
        this.height = height;

        this.currX = hiddenX;
        this.targetX = hiddenX;

        upgradeUIHeight = (height - TITLE_TEXT_HEIGHT - MARGIN * 2) / 4 - MARGIN;
        upgradeUIs = new UpgradeUI[4];
        upgradeUIs[0] = new UpgradeUI(0, 0, width - MARGIN * 2, upgradeUIHeight, "Capacity",
                playerStats.CAPACITY_UPGRADE_COST, playerStats::getCapacity, playerStats::upgradeCapacity);
        upgradeUIs[1] = new UpgradeUI(0, 0, width - MARGIN * 2, upgradeUIHeight, "Movement Speed",
                playerStats.MOVEMENT_SPEED_UPGRADE_COST, playerStats::getMovementSpeed,
                playerStats::upgradeMovementSpeed);
        upgradeUIs[2] = new UpgradeUI(0, 0, width - MARGIN * 2, upgradeUIHeight, "Door Speed",
                playerStats.DOOR_SPEED_UPGRADE_COST, playerStats::getDoorSpeed, playerStats::upgradeDoorSpeed);
        upgradeUIs[3] = new UpgradeUI(0, 0, width - MARGIN * 2, upgradeUIHeight, "People Speed",
                playerStats.PEOPLE_SPEED_UPGRADE_COST, playerStats::getPeopleSpeed, playerStats::upgradePeopleSpeed);
        updateUpgradeUIPositions();
    }

    public void draw(PApplet d) {
        // Update position
        updatePanelPosition();
        updateUpgradeUIPositions();

        // If panel is off the screen, don't draw
        if (currX == hiddenX)
            return;

        // Save original settings
        d.push();

        // Blur area behind panel
        Main.blur(d, currX, y, width, height, 2.5F);

        // Draw Rect
        d.fill(250, 200F);
        d.stroke(0);
        d.strokeWeight(3);
        d.rect(currX, y, width, height, 8);

        // Draw Title
        d.fill(0);
        d.textSize(TITLE_TEXT_SIZE);
        d.textAlign(PConstants.CENTER, PConstants.TOP);
        d.text("Upgrades", currX + width / 2, y + 20);

        // Draw Upgrades
        for (UpgradeUI ui : upgradeUIs) {
            ui.draw(d);
        }

        // Restore original settings
        d.pop();
    }

    public boolean mousePressed(int x, int y) {
        for (UpgradeUI uui : upgradeUIs) {
            uui.mousePressed(x, y);
        }

        return x >= currX && x <= currX + width && y >= this.y && y <= this.y + height;
    }

    public void toggleVisible() {
        visible = !visible;
        targetX = visible ? visibleX : hiddenX;
    }

    private void updatePanelPosition() {
        final int SPEED = 70;

        if (Math.abs(currX - targetX) < SPEED) {
            currX = targetX;
            return;
        }

        if (currX < targetX) {
            currX += SPEED;
        } else if (currX > targetX) {
            currX -= SPEED;
        }
    }

    private void updateUpgradeUIPositions() {
        upgradeUIs[0].updatePosition(currX + MARGIN,
                y + TITLE_TEXT_HEIGHT + MARGIN * 2 + (upgradeUIHeight + MARGIN) * 0);
        upgradeUIs[1].updatePosition(currX + MARGIN,
                y + TITLE_TEXT_HEIGHT + MARGIN * 2 + (upgradeUIHeight + MARGIN) * 1);
        upgradeUIs[2].updatePosition(currX + MARGIN,
                y + TITLE_TEXT_HEIGHT + MARGIN * 2 + (upgradeUIHeight + MARGIN) * 2);
        upgradeUIs[3].updatePosition(currX + MARGIN,
                y + TITLE_TEXT_HEIGHT + MARGIN * 2 + (upgradeUIHeight + MARGIN) * 3);
    }

    private class UpgradeUI implements ButtonListener {
        // Interface for getting a player stat
        // Used for a lambda expression
        private interface StatGetter {
            int getStat();
        }

        // Interface for getting a player stat
        // Used for a lambda expression
        private interface StatUpgrader {
            boolean upgradeStat();
        }

        private static final double MARGIN_WIDTH_PER = 0.1;
        private static final double NAME_WIDTH_PER = 0.65;
        private static final double NAME_HEIGHT_PER = 0.5;
        private static final double MIDDLE_GAP_HEIGHT_PER = 0.18; // gap between bottom of button and top of progress
                                                                  // bar
        private static final double UPGRADE_BAR_MARGIN_WIDTH_PER = 0.05;
        private static final double UPGRADE_BAR_MARGIN_HEIGHT_PER = 0.1;
        private static final double UPGRADE_BAR_WIDTH_PER = (1 - UPGRADE_BAR_MARGIN_WIDTH_PER * 4) / 5;
        private static final double UPGRADE_BAR_HEIGHT_PER = (1 - NAME_HEIGHT_PER) / 4;

        private int x;
        private int y;

        private int marginWidth;
        private int nameWidth;
        private int nameHeight;
        private int middleGapHeight;
        private int upgradeBarMarginWidth;
        private int upgradeBarMarginHeight;
        private int upgradeBarWidth;
        private int upgradeBarHeight;
        private int costWidth;
        private int costHeight;

        private String upgradeName;

        private Button costButton;

        private StatGetter statGetter;
        private StatUpgrader statUpgrader;

        private UpgradeUI(int x, int y, int width, int height, String upgradeName, int upgradeCost,
                StatGetter statGetter, StatUpgrader statUpgrader) {
            this.upgradeName = upgradeName;
            this.statGetter = statGetter;
            this.statUpgrader = statUpgrader;

            marginWidth = (int) (width * MARGIN_WIDTH_PER);
            nameWidth = (int) (width * NAME_WIDTH_PER);
            nameHeight = (int) (height * NAME_HEIGHT_PER);
            middleGapHeight = (int) (height * MIDDLE_GAP_HEIGHT_PER);
            upgradeBarMarginWidth = (int) (width * UPGRADE_BAR_MARGIN_WIDTH_PER);
            upgradeBarMarginHeight = (int) (height * UPGRADE_BAR_MARGIN_HEIGHT_PER);
            upgradeBarWidth = (int) (width * UPGRADE_BAR_WIDTH_PER);
            upgradeBarHeight = (int) (height * UPGRADE_BAR_HEIGHT_PER);
            costWidth = upgradeBarWidth * 5 + upgradeBarMarginWidth * 4 - nameWidth - marginWidth;
            costHeight = nameHeight;

            costButton = new Button(0, 0, costWidth - 3, costHeight);
            costButton.setText(String.valueOf(upgradeCost));
            costButton.setStrokeWeight(2);
            costButton.addListener(this);

            this.updatePosition(x, y);
        }

        private void draw(PApplet d) {
            d.push(); // Save original settings

            // Name
            d.fill(0);
            d.textSize(20);
            d.textAlign(PConstants.LEFT, PConstants.CENTER);
            d.rectMode(PConstants.CORNER);
            d.text(upgradeName, x, y, nameWidth, nameHeight);

            // Cost
            costButton.draw(d);

            // Progress Bar
            d.strokeWeight(2);
            int rectX = x;
            int rectY = y + nameHeight + middleGapHeight;
            int stat = statGetter.getStat();
            for (int row = 0; row < 2; row++) {
                rectX = x;
                for (int col = 0; col < 5; col++) {
                    int rectNum = row * 5 + col;
                    d.fill(rectNum < stat ? 0 : 255);
                    d.rect(rectX, rectY, upgradeBarWidth, upgradeBarHeight);
                    rectX += upgradeBarWidth + upgradeBarMarginWidth;
                }
                rectY += upgradeBarHeight + upgradeBarMarginHeight;
            }

            d.pop(); // Restore original settings
        }

        private void mousePressed(int x, int y) {
            costButton.mousePressed(x, y);
        }

        private void updatePosition(int x, int y) {
            this.x = x;
            this.y = y;
            costButton.setPosition(x + nameWidth + marginWidth, y);
        }

        // Implementing ButtonListener
        public void onClick(Button button) {
            if (!button.equals(costButton))
                return;

            statUpgrader.upgradeStat();
        }

    }

}
