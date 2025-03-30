package Screens;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PConstants;
import Elements.Button.Button;
import Elements.Button.Button.ButtonListener;
import Main.DataHolder;
import Main.Main;

public class LevelSelect implements Screen, ButtonListener {

    private final int MARGIN = 50;

    private ArrayList<Button> levelButtons;
    private Button level1Button;
    private Button level2Button;
    private Button level3Button;

    public LevelSelect() {
        int bWidth = (int) ((Main.WINDOW_WIDTH - MARGIN * 4) / 3.0);
        int bHeight = 80;
        int bTextSize = 32;

        level1Button = new Button(MARGIN, Main.WINDOW_HEIGHT / 2 - bHeight / 2, bWidth, bHeight);
        level1Button.setText("Easy");

        level2Button = new Button(MARGIN * 2 + bWidth, Main.WINDOW_HEIGHT / 2 - bHeight / 2, bWidth, bHeight);
        level2Button.setText("Medium");

        level3Button = new Button(MARGIN * 3 + bWidth * 2, Main.WINDOW_HEIGHT / 2 - bHeight / 2, bWidth, bHeight);
        level3Button.setText("Hard");

        levelButtons = new ArrayList<>();
        levelButtons.add(level1Button);
        levelButtons.add(level2Button);
        levelButtons.add(level3Button);

        for (Button b : levelButtons) {
            b.setTextSize(bTextSize);
            b.addListener(this);
        }
    }

    @Override
    public void draw(PApplet d) {
        d.push(); // Save original settings

        d.background(255);

        // Game Title
        Main.drawGameTitle(d);

        // Select Difficulty Text
        d.fill(0);
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.textFont(DataHolder.getMediumFont());
        d.textSize(24);
        d.text("Select Difficulty!", 480, 120);

        // Level Buttons
        for (Button b : levelButtons) {
            b.draw(d);
        }

        d.pop(); // Restore original settings
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        for (Button b : levelButtons) {
            b.mousePressed(mouseX, mouseY);
        }
    }

    @Override
    public void keyPressed(char key) {
        // Do nothing
    }

    @Override
    public void onClick(Button button) {
        int level = levelButtons.indexOf(button) + 1;
        Main.getInstance().startLevel(level);
    }

}
