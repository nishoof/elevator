package Screens;

import Elements.Button.Button;
import Main.DataHolder;
import Main.Main;
import processing.core.PApplet;
import processing.core.PConstants;

public class Menu implements Screen {

    private Button playButton;

    public Menu() {
        playButton = new Button(380, 430, 200, 80);
        playButton.setTextSize(32);
        playButton.setText("Play!");
    }

    @Override
    public void draw(PApplet d) {
        d.push();          // Save original settings

        d.background(255);

        // Game Title
        Main.drawGameTitle(d);

        // Instructions
        d.fill(0);
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.textFont(DataHolder.getMediumFont());
        d.textSize(24);
        d.text("How to play!", 480, 120);
        d.textFont(DataHolder.getRegularFont());
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
        d.textFont(DataHolder.getMediumFont());
        playButton.draw(d);
        d.textSize(32);
        d.text("Play!", 480, 470);

        d.pop();           // Restore original settings
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        playButton.mousePressed(mouseX, mouseY);
    }

    @Override
    public void keyPressed(char key) {
        // Do nothing
    }

    public Button getPlayButton() {
        return playButton;
    }

}
