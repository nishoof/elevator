package Elements;

import Elements.Button.Button;
import Elements.Button.ButtonListener;
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
    private UpgradeUI[] upgradeUIs;

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

        upgradeUIs = new UpgradeUI[4];
        upgradeUIs[0] = new UpgradeUI(x + MARGIN, y + TITLE_TEXT_HEIGHT + MARGIN*2 + (buttonHeight + MARGIN)*0, width - MARGIN*2, buttonHeight, "Capacity", playerStats.CAPACITY_UPGRADE_COST, playerStats::getCapacity, playerStats::upgradeCapacity);
        upgradeUIs[1] = new UpgradeUI(x + MARGIN, y + TITLE_TEXT_HEIGHT + MARGIN*2 + (buttonHeight + MARGIN)*1, width - MARGIN*2, buttonHeight, "Movement Speed", playerStats.MOVEMENT_SPEED_UPGRADE_COST, playerStats::getMovementSpeed, playerStats::upgradeMovementSpeed);
        upgradeUIs[2] = new UpgradeUI(x + MARGIN, y + TITLE_TEXT_HEIGHT + MARGIN*2 + (buttonHeight + MARGIN)*2, width - MARGIN*2, buttonHeight, "Door Speed", playerStats.DOOR_SPEED_UPGRADE_COST, playerStats::getDoorSpeed, playerStats::upgradeDoorSpeed);
        upgradeUIs[3] = new UpgradeUI(x + MARGIN, y + TITLE_TEXT_HEIGHT + MARGIN*2 + (buttonHeight + MARGIN)*3, width - MARGIN*2, buttonHeight, "People Speed", playerStats.PEOPLE_SPEED_UPGRADE_COST, playerStats::getPeopleSpeed, playerStats::upgradePeopleSpeed);
    }

    public void draw(PApplet d) {
        if (!visible) return;

        d.push();          // Save original settings

        // Draw Rect
        d.fill(250);
        d.stroke(0);
        d.strokeWeight(3);
        d.rect(x, y, width, height, 8);

        // Draw Title
        d.fill(0);
        d.textSize(TITLE_TEXT_SIZE);
        d.textAlign(PConstants.CENTER, PConstants.TOP);
        d.text("Upgrades", x + width/2, y + 20);

        // Draw Upgrades
        for (UpgradeUI ui : upgradeUIs) {
            ui.draw(d);
        }

        d.pop();           // Restore original settings
    }

    public void mousePressed(int x, int y) {
        if (!visible) return;

        // for (UpgradeButton b : upgradeButtons) {
        //     b.mousePressed(x, y);
        // }

        for (UpgradeUI uui : upgradeUIs) {
            uui.mousePressed(x, y);
        }
    }

    public void toggleVisible() {
        visible = !visible;
    }

    private class UpgradeUI implements ButtonListener {
        // Interface for getting a player stat
        // Used for a lambda expression
        public interface StatGetter {
            int getStat();
        }

        // Interface for getting a player stat
        // Used for a lambda expression
        public interface StatUpgrader {
            boolean upgradeStat();
        }

        private static final double MARGIN_WIDTH_PER = 0.1;
        private static final double NAME_WIDTH_PER = 0.65;
        private static final double NAME_HEIGHT_PER = 0.5;
        private static final double MIDDLE_GAP_HEIGHT_PER = 0.18;           // gap between bottom of button and top of progress bar
        private static final double UPGRADE_BAR_MARGIN_WIDTH_PER = 0.05;
        private static final double UPGRADE_BAR_MARGIN_HEIGHT_PER = 0.1;
        private static final double UPGRADE_BAR_WIDTH_PER = (1 - UPGRADE_BAR_MARGIN_WIDTH_PER*4) / 5;
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

        private UpgradeUI(int x, int y, int width, int height, String upgradeName, int upgradeCost, StatGetter statGetter, StatUpgrader statUpgrader) {
            this.x = x;
            this.y = y;

            this.upgradeName = upgradeName;
            this.statGetter = statGetter;
            this.statUpgrader = statUpgrader;

            marginWidth = (int)(width * MARGIN_WIDTH_PER);
            nameWidth = (int)(width * NAME_WIDTH_PER);
            nameHeight = (int)(height * NAME_HEIGHT_PER);
            middleGapHeight = (int)(height * MIDDLE_GAP_HEIGHT_PER);
            upgradeBarMarginWidth = (int)(width * UPGRADE_BAR_MARGIN_WIDTH_PER);
            upgradeBarMarginHeight = (int)(height * UPGRADE_BAR_MARGIN_HEIGHT_PER);
            upgradeBarWidth = (int)(width * UPGRADE_BAR_WIDTH_PER);
            upgradeBarHeight = (int)(height * UPGRADE_BAR_HEIGHT_PER);;
            costWidth = upgradeBarWidth*5 + upgradeBarMarginWidth*4 - nameWidth - marginWidth;      // accounts for the int rounding so everything lines up perfectly :)
            costHeight = nameHeight;

            costButton = new Button(x + nameWidth + marginWidth, y, costWidth - 3, costHeight);
            costButton.setText(String.valueOf(upgradeCost));
            costButton.setStrokeWeight(2);
            costButton.addListener(this);
        }

        private void draw(PApplet d) {
            d.push();          // Save original settings

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
                    int rectNum = row*5 + col;
                    d.fill(rectNum < stat ? 0 : 255);
                    d.rect(rectX, rectY, upgradeBarWidth, upgradeBarHeight);
                    rectX += upgradeBarWidth + upgradeBarMarginWidth;
                }
                rectY += upgradeBarHeight + upgradeBarMarginHeight;
            }

            d.pop();           // Restore original settings
        }

        private void mousePressed(int x, int y) {
            costButton.mousePressed(x, y);
        }

        // Implementing ButtonListener
        public void onClick(Button button) {
            if (!button.equals(costButton)) return;

            statUpgrader.upgradeStat();
        }

    }

}
