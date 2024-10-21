package Screens;

import java.util.ArrayList;
import Elements.Button.Button;
import Elements.Button.UpgradeButton;
import Main.DataHolder;
import Main.Main;
import Main.PlayerStats;
import processing.core.PApplet;
import processing.core.PConstants;

public class Upgrades implements Screen {

    private Button closeButton;

    private ArrayList<UpgradeButton> upgradeButtons;
    private UpgradeButton capacityUpgradeButton;
    private UpgradeButton movementSpeedUpgradeButton;
    private UpgradeButton doorSpeedUpgradeButton;
    private UpgradeButton peopleSpeedUpgradeButton;

    private final int MARGIN = 30;

    public Upgrades() {
        // Close button
        closeButton = new Button(Main.WINDOW_WIDTH-MARGIN-26, 30, 26, 26);
        closeButton.setStroke(0);
        closeButton.setFillColor(255, 0, 0);
        closeButton.setHoveredColor(204, 0, 0);
        closeButton.setStrokeWeight(0);
        closeButton.setCornerRounding(0);

        // Upgrade buttons
        int buttonWidth = (int)((Main.WINDOW_WIDTH - MARGIN*4)/3.0);
        
        capacityUpgradeButton = new UpgradeButton(MARGIN, 75, buttonWidth, 128, "Capacity", PlayerStats::getCapacity, PlayerStats::upgradeCapacity, PlayerStats.CAPACITY_UPGRADE_COST);
        movementSpeedUpgradeButton = new UpgradeButton(MARGIN*2 + buttonWidth, 75, buttonWidth, 128, "Movement Speed", PlayerStats::getMovementSpeed, PlayerStats::upgradeMovementSpeed, PlayerStats.MOVEMENT_SPEED_UPGRADE_COST);
        doorSpeedUpgradeButton = new UpgradeButton(MARGIN*3 + buttonWidth*2, 75, buttonWidth, 128, "Door Speed", PlayerStats::getDoorSpeed, PlayerStats::upgradeDoorSpeed, PlayerStats.DOOR_SPEED_UPGRADE_COST);
        peopleSpeedUpgradeButton = new UpgradeButton(MARGIN, 225, buttonWidth, 128, "People Speed", PlayerStats::getPeopleSpeed, PlayerStats::upgradePeopleSpeed, PlayerStats.PEOPLE_SPEED_UPGRADE_COST);
        
        upgradeButtons = new ArrayList<>();
        upgradeButtons.add(capacityUpgradeButton);
        upgradeButtons.add(movementSpeedUpgradeButton);
        upgradeButtons.add(doorSpeedUpgradeButton);
        upgradeButtons.add(peopleSpeedUpgradeButton);
    }

    @Override
    public void draw(PApplet d) {
        d.push();          // Save original settings

        d.background(255);
        
        // Draw the "Upgrades" title
        d.textFont(DataHolder.getMediumFont());
        d.fill(0);
        d.textSize(32);
        d.textAlign(PConstants.CENTER, PConstants.TOP);
        d.text("Upgrades", Main.WINDOW_WIDTH / 2, 30);
        
        // Draw the "Close" button
        closeButton.draw(d);

        // Draw the upgrade buttons
        for (UpgradeButton upgradeButton : upgradeButtons) {
            upgradeButton.draw(d);
        }

        // Show the current credits
        d.textFont(DataHolder.getRegularFont());
        d.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
        d.textSize(24);
        d.fill(0);
        d.text("Credits: " + PlayerStats.getCredits(), 940, 520);

        d.pop();           // Restore original settings
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        // Close button
        if (mouseX >= 910 && mouseX <= 945 && mouseY >= 15 && mouseY <= 50) {
            Main.getInstance().toggleUpgradesScreen();
            return;
        }

        // Upgrade buttons
        for (UpgradeButton upgradeButton : upgradeButtons) {
            upgradeButton.mousePressed(mouseX, mouseY);
        }
    }

    @Override
    public void keyPressed(char key) {
        // Do nothing
    }

}
