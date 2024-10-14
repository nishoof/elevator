import processing.core.PApplet;
import processing.core.PConstants;

public class Menu {

    private boolean playButtonPressed;

    public Menu() {
        playButtonPressed = false;
    }

    public void draw(PApplet d) {
        d.push();          // Save original settings

        // Game Title
        d.strokeWeight(0);
        d.textFont(FontHolder.getRegular());
        d.fill(0);
        d.rect(280, 25, 400, 50);      // outer black rect
        d.textSize(32);
        d.fill(255);
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.text("Elevator Simulator", 285, 50);
        d.rect(645, 35, 20, 30);      // small white rectangle symbol

        // Instructions
        d.fill(0);
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.textFont(FontHolder.getMedium());
        d.textSize(24);
        d.text("How to play!", 480, 120);
        d.textFont(FontHolder.getRegular());
        d.textSize(19);
        d.text("- blah blah blah", 480, 149);
        d.text("- yap yap yap", 480, 173);

        // Play button at the center bottom
        d.fill(255);
        d.strokeWeight(3);
        d.rectMode(PConstants.CENTER);
        d.rect(480, 400, 200, 80, 8);      // outer black rect
        d.fill(0);
        d.textFont(FontHolder.getMedium());
        d.textSize(32);
        d.text("Play!", 480, 400);

        d.pop();           // Restore original settings
    }

    public void mousePressed(int mouseX, int mouseY) {
        // If play button was pressed, update playButtonPressed
        if (mouseX >= 380 && mouseX <= 580 && mouseY >= 360 && mouseY <= 440) {
            playButtonPressed = true;
            System.out.println("Play button pressed!");
        }
    }

    public boolean playButtonPressed() {
        return playButtonPressed;
    }

}
