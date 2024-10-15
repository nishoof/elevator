package Screens;

// import java.awt.Point;

import Main.FontHolder;
import Main.Main;
import Main.PlayerStats;
import processing.core.PApplet;
import processing.core.PConstants;

public class Upgrades implements Screen {

    // private boolean elevatorFloorsUpgradeHovered;

    public Upgrades() {}

    @Override
    public void draw(PApplet d) {
        // Check if the mouse is hovering over anything
        // Point mouse = Main.getScaledMouse(d);
        // elevatorFloorsUpgradeHovered = mouse.x > 15 && mouse.x < 315 && mouse.y > 75 && mouse.y < 203;

        d.push();          // Save original settings

        // Draw a rectangle that covers the screen
        d.background(255);
        
        // Draw the "Upgrades" title
        d.textFont(FontHolder.getMedium());
        d.fill(0);
        d.textSize(32);
        d.textAlign(PConstants.CENTER, PConstants.TOP);
        d.text("Upgrades", Main.WINDOW_WIDTH / 2, 30);
        
        // Draw the "Close" button
        d.fill(255, 0, 0);
        d.noStroke();
        d.rectMode(PConstants.CORNER);
        d.rect(910, 15, 35, 35);

        // Show the current credits
        d.textFont(FontHolder.getRegular());
        d.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
        d.textSize(24);
        d.fill(0);
        d.text("Credits: " + PlayerStats.getCredits(), 940, 520);
        
        // Draw the elevator capacity upgrade
        // d.fill(elevatorFloorsUpgradeHovered ? 225 : 240);
        d.fill(240);
        d.rect(15, 75, 300, 128);
        d.fill(0);
        d.textSize(24);
        d.textAlign(PConstants.LEFT, PConstants.TOP);
        d.text("Capacity", 22, 85);
        d.textSize(20);
        d.text("Current: " + PlayerStats.getCapacity(), 22, 117);
        d.text("Upgrade to: " + (PlayerStats.getCapacity() + 1), 22, 145);
        d.text("Cost: " + PlayerStats.CAPACITY_UPGRADE_COST, 22, 173);

        // Draw the elevator movement speed upgrade
        d.fill(240);
        d.rect(335, 75, 300, 128);
        d.fill(0);
        d.textSize(24);
        d.text("Movement speed", 342, 85);
        d.textSize(20);
        d.text("Current: " + PlayerStats.getMovementSpeed(), 342, 117);
        d.text("Upgrade to: " + (PlayerStats.getMovementSpeed() + 1), 342, 145);
        d.text("Cost: " + PlayerStats.MOVEMENT_SPEED_UPGRADE_COST, 342, 173);

        // Draw the elevator movement speed upgrade
        d.fill(240);
        d.rect(655, 75, 300, 128);
        d.fill(0);
        d.textSize(24);
        d.text("Door speed", 662, 85);
        d.textSize(20);
        d.text("Current: " + PlayerStats.getDoorSpeed(), 662, 117);
        d.text("Upgrade to: " + (PlayerStats.getDoorSpeed() + 1), 662, 145);
        d.text("Cost: " + PlayerStats.DOOR_SPEED_UPGRADE_COST, 662, 173);

        // Draw the people speed upgrade
        d.fill(240);
        d.rect(15, 225, 300, 128);
        d.fill(0);
        d.textSize(24);
        d.textAlign(PConstants.LEFT, PConstants.TOP);
        d.text("People speed", 22, 235);
        d.textSize(20);
        d.text("Current: " + PlayerStats.getCapacity(), 22, 267);
        d.text("Upgrade to: " + (PlayerStats.getCapacity() + 1), 22, 295);
        d.text("Cost: " + PlayerStats.CAPACITY_UPGRADE_COST, 22, 323);
        
        d.pop();           // Restore original settings
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        // Close button
        if (mouseX >= 910 && mouseX <= 945 && mouseY >= 15 && mouseY <= 50) {
            Main.getInstance().switchScreen(Main.GAME);
            return;
        }

        // Elevator capacity upgrade
        if (mouseX > 15 && mouseX < 315 && mouseY > 75 && mouseY < 203) {
            PlayerStats.upgradeCapacity();
        }

        // Elevator movement speed upgrade
        if (mouseX > 335 && mouseX < 635 && mouseY > 75 && mouseY < 203) {
            PlayerStats.upgradeMovementSpeed();
        }

        // Elevator door speed upgrade
        if (mouseX > 655 && mouseX < 955 && mouseY > 75 && mouseY < 203) {
            PlayerStats.upgradeDoorSpeed();
        }

        // People speed upgrade
        if (mouseX > 15 && mouseX < 315 && mouseY > 225 && mouseY < 353) {
            PlayerStats.upgradePeopleSpeed();
        }
    }

    @Override
    public void keyPressed(char key) {
        // Do nothing
    }

}
