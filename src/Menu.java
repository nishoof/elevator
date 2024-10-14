import java.awt.Point;

import processing.core.PApplet;
import processing.core.PConstants;

public class Menu implements Screen {

    private boolean playButtonPressed;

    private final int playButtonX = 380;
    private final int playButtonY = 430;
    private final int playButtonWidth = 200;
    private final int playButtonHeight = 80;

    public Menu() {
        playButtonPressed = false;
    }

    @Override
    public void draw(PApplet d) {
        d.push();          // Save original settings

        Point mouse = Main.getScaledMouse(d);
        boolean playButtonIsHovered = buttonContains(mouse.x, mouse.y);

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
        d.textSize(18);
        int s1 = 23;            // spacing between every line
        int s2 = 5;             // spacing between every different sentence
        d.text("The queue on the left shows people that want",      480, 155 + s1*0 + s2*0);
        d.text("to go to a different floor",                        480, 155 + s1*1 + s2*0);
        d.text("For example, 1 -> 2 means there's a person at ",    480, 155 + s1*2 + s2*1);
        d.text("floor 1 that wants to go to floor 2",               480, 155 + s1*3 + s2*1);
        d.text("To move the elevator, just click the buttons",      480, 155 + s1*4 + s2*2);
        d.text("on the right",                                      480, 155 + s1*5 + s2*2);
        d.text("You can also use your keyboard if you want to:",    480, 155 + s1*6 + s2*3);
        d.text("1) Select the elevator using q, w, e, ...",         480, 155 + s1*7 + s2*3);
        d.text("2) Type the floor number you want",                 480, 155 + s1*8 + s2*3);
        d.text("Have fun!",                                         480, 155 + s1*9 + s2*4);

        // Play button
        d.fill(playButtonIsHovered ? 235 : 250);
        d.strokeWeight(3);
        d.rectMode(PConstants.CORNER);
        d.rect(playButtonX, playButtonY, playButtonWidth, playButtonHeight, 8);      // outer black rect
        d.fill(0);
        d.textFont(FontHolder.getMedium());
        d.textSize(32);
        d.text("Play!", 480, 470);

        d.pop();           // Restore original settings
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        // If play button was pressed, update playButtonPressed
        if (buttonContains(mouseX, mouseY)) {
            playButtonPressed = true;
        }
    }

    @Override
    public void keyPressed(char key) {
        // Do nothing
    }

    public boolean playButtonPressed() {
        return playButtonPressed;
    }

    private boolean buttonContains(int x, int y) {
        if (x < playButtonX) return false;
        if (y < playButtonY) return false;
        if (x > playButtonX + playButtonWidth) return false;
        if (y > playButtonY + playButtonHeight) return false;
        return true;
    }

}