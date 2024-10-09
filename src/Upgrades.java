import java.awt.Point;

import processing.core.PApplet;
import processing.core.PConstants;

public class Upgrades {

    private final int ELEVATOR_FLOOR_COST = 10;

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
        Point mouse = game.getMouse();
        elevatorFloorsUpgradeHovered = mouse.x > 15 && mouse.x < 315 && mouse.y > 75 && mouse.y < 203;

        d.push();          // Save original settings

        // Draw a rectangle that covers the screen
        d.background(255);
        
        // Draw the "Upgrades" title
        d.textFont(FontHolder.getMedium());
        d.fill(0);
        d.textSize(32);
        d.textAlign(PConstants.CENTER, PConstants.TOP);
        d.text("Upgrades", d.width / 2, 30);
        
        // Draw the "Close" button
        d.fill(255, 0, 0);
        d.noStroke();
        d.rect(d.width - 50, 15, 35, 35);

        // Show the current credits
        d.textFont(FontHolder.getRegular());
        d.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
        d.textSize(24);
        d.fill(0);
        d.text("Credits: " + game.getCredits(), game.width - 20, game.height - 20);
        
        // Draw the elevator floors upgrade
        d.fill(elevatorFloorsUpgradeHovered ? 225 : 240);
        d.rect(15, 75, 300, 128);
        d.fill(0);
        d.textSize(24);
        d.textAlign(PConstants.LEFT, PConstants.TOP);
        d.text("Elevator Floors", 22, 85);
        d.textSize(20);
        d.text("Current: " + game.getNumFloors(), 22, 117);
        d.text("Upgrade to: " + (game.getNumFloors() + 1), 22, 145);
        d.text("Cost: " + ELEVATOR_FLOOR_COST, 22, 173);
        
        d.pop();           // Restore original settings
    }

    public void mousePressed(int mouseX, int mouseY) {
        if (!menuOpen) return;

        // Close button
        if (mouseX > game.width - 50 && mouseX < game.width - 35 && mouseY > 15 && mouseY < 85) {
            closeMenu();
        }

        // Elevator floors upgrade
        if (mouseX > 15 && mouseX < 315 && mouseY > 75 && mouseY < 203) {
            if (game.getCredits() < ELEVATOR_FLOOR_COST) {
                System.out.println("Not enough credits to upgrade elevator floors.");
            } else {
                game.spendCredits(ELEVATOR_FLOOR_COST);
                game.increaseFloorCount();
            }
        }
    }

    // int scaledMouseX = (int)(1.0 * mouseX / width * WINDOW_SIZE * X_RATIO);
    //     int scaledMouseY = (int)(1.0 * mouseY / height * WINDOW_SIZE * Y_RATIO);

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
