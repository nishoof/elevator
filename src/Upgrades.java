import java.awt.Point;

import processing.core.PApplet;
import processing.core.PConstants;

public class Upgrades {

    private final int CAPACITY_UPGRADE_COST = 10;

    private Game game;

    private boolean menuOpen;

    private boolean elevatorFloorsUpgradeHovered;

    public Upgrades(Game game) {
        this.game = game;
        this.menuOpen = false;
    }

    public void draw(PApplet d) {
        // If the menu is not open, don't draw anything
        if (!menuOpen) return;

        // Check if the mouse is hovering over anything
        Point mouse = Main.getScaledMouse(d);
        elevatorFloorsUpgradeHovered = mouse.x > 15 && mouse.x < 315 && mouse.y > 75 && mouse.y < 203;

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
        d.rect(910, 15, 35, 35);

        // Show the current credits
        d.textFont(FontHolder.getRegular());
        d.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
        d.textSize(24);
        d.fill(0);
        d.text("Credits: " + game.getCredits(), 940, 520);
        
        // Draw the elevator capacity upgrade
        d.fill(elevatorFloorsUpgradeHovered ? 225 : 240);
        d.rect(15, 75, 300, 128);
        d.fill(0);
        d.textSize(24);
        d.textAlign(PConstants.LEFT, PConstants.TOP);
        d.text("Elevator capacity", 22, 85);
        d.textSize(20);
        d.text("Current: " + game.getNumFloors(), 22, 117);
        d.text("Upgrade to: " + (game.getNumFloors() + 1), 22, 145);
        d.text("Cost: " + CAPACITY_UPGRADE_COST, 22, 173);
        
        d.pop();           // Restore original settings
    }

    public void mousePressed(int mouseX, int mouseY) {
        if (!menuOpen) return;

        // Close button
        if (mouseX > Main.WINDOW_WIDTH - 50 && mouseX < Main.WINDOW_WIDTH - 35 && mouseY > 15 && mouseY < 85) {
            closeMenu();
        }

        // Elevator floors upgrade
        if (mouseX > 15 && mouseX < 315 && mouseY > 75 && mouseY < 203) {
            if (game.getCredits() < CAPACITY_UPGRADE_COST) {
                System.out.println("Not enough credits.");
            } else {
                game.spendCredits(CAPACITY_UPGRADE_COST);
                game.increaseFloorCount();
            }
        }
    }

    public void toggleMenuDisplay() {
        menuOpen = !menuOpen;
    }

    public void openMenu() {
        menuOpen = true;
    }

    public void closeMenu() {
        menuOpen = false;
    }

}
