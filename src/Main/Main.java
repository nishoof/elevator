package Main;

import java.util.ArrayList;

import Elements.Button.Button;
import Elements.Button.ButtonListener;
import Screens.Game;
import Screens.LevelSelect;
import Screens.Menu;
import Screens.Screen;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class Main extends PApplet implements ButtonListener {

    // Window Constants (just for reference, don't change)
    public static final int X_RATIO = 16;
    public static final int Y_RATIO = 9;
    public static final int WINDOW_SIZE = 60;
    public static final int WINDOW_WIDTH = WINDOW_SIZE * X_RATIO;
    public static final int WINDOW_HEIGHT = WINDOW_SIZE * Y_RATIO;

    // Screen IDs
    public static final int MENU = 0;
    public static final int LEVEL_SELECT = 1;
    public static final int GAME = 2;

    // Instance of main
    private static Main instance;

    // Screen
    private int currentScreen;
    private ArrayList<Screen> screens;

    // Screen Switching Buttons
    private Button menuPlayButton;
    private Button returnToMenuButton;

    public Main() {
        if (instance != null)
            throw new IllegalStateException("There can only be one instance of Main");
        instance = this;
    }

    public static void main(String[] args) {
        PApplet.main("Main.Main");
    }

    @Override
    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
        smooth();
    }

    @Override
    public void setup() {
        // Window
        windowResizable(true);
        windowTitle("Elevator Simulator");

        // Load all resources (images and fonts)
        DataHolder.init(this);

        // Return to Menu Button
        returnToMenuButton = new Button(307, 275, 346, 70);
        returnToMenuButton.setTextSize(28);
        returnToMenuButton.setText("Return to Menu!");
        returnToMenuButton.addListener(this);

        // Screens
        currentScreen = MENU;
        screens = new ArrayList<>();
        screens.add(new Menu());
        screens.add(new LevelSelect());
        screens.add(null);

        // Menu Play Button
        menuPlayButton = ((Menu) (screens.get(MENU))).getPlayButton();
        menuPlayButton.addListener(this);
    }

    @Override
    public void draw() {
        windowRatio(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Draw current screen
        screens.get(currentScreen).draw(this);
    }

    @Override
    public void mousePressed() {
        screens.get(currentScreen).mousePressed(rmouseX, rmouseY);
    }

    @Override
    public void keyPressed() {
        screens.get(currentScreen).keyPressed(key);
    }

    @Override
    // Implementing ButtonListener method
    public void onClick(Button button) {
        if (button == menuPlayButton) {
            switchScreen(LEVEL_SELECT);
        } else if (button == returnToMenuButton) {
            switchScreen(MENU);
        } else {
            throw new IllegalArgumentException("Button not recognized");
        }
    }

    public void switchScreen(int screen) {
        currentScreen = screen;
    }

    public void switchScreen(Screen screen) {
        switchScreen(screens.indexOf(screen));
    }

    public void startLevel(int level) {
        // Make the new game
        int[][] waves = new int[][] { { 3, 3000, 3000 }, { 5, 2500, 2500 }, { 10, 750, 1250 } };
        Game game;
        switch (level) {
            case 1:
                game = new Game(1, 3, waves);
                break;
            case 2:
                game = new Game(1, 7, waves);
                break;
            case 3:
                game = new Game(2, 10, waves);
                break;
            default:
                throw new IllegalArgumentException("Level must be between 1 and 3");
        }
        screens.set(GAME, game);

        // Start the game for the level
        game.startTime();

        // Switch to the screen
        switchScreen(GAME);
    }

    public Button getReturnToMenuButton() {
        return returnToMenuButton;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void drawGameTitle(PApplet d) {
        d.push(); // Save original settings

        d.strokeWeight(0);
        d.textFont(DataHolder.getRegularFont());
        d.fill(0);
        d.rect(280, 25, 400, 50); // outer black rect
        d.textSize(32);
        d.fill(255);
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.text("Elevator Simulator", 285, 50);
        d.rect(645, 35, 20, 30); // small white rectangle symbol

        d.pop(); // Restore original settings
    }

    /**
     * Blurs the existing drawing on the screen within the specified rectangle
     *
     * @param d            PApplet
     * @param x            x-coordinate of the top-left corner of the rectangle
     * @param y            y-coordinate of the top-left corner of the rectangle
     * @param width        width of the rectangle
     * @param height       height of the rectangle
     * @param blurStrength strength of the blur
     */
    public static void blur(PApplet d, int x, int y, int width, int height, float blurStrength) {
        float deadSpaceX = d.width - d.rwidth * d.ratioScale;
        float deadSpaceY = d.height - d.rheight * d.ratioScale;
        int realX = (int) ((1.0 * x * d.ratioScale) + (deadSpaceX / 2));
        int realY = (int) ((1.0 * y * d.ratioScale) + (deadSpaceY / 2));
        int realWidth = (int) (width * d.ratioScale);
        int realHeight = (int) (height * d.ratioScale);

        PGraphics g = d.createGraphics(realWidth, realHeight);
        g.beginDraw();
        g.loadPixels();

        d.loadPixels();
        int graphicsIndex = 0;

        for (int pixY = 0; pixY < realHeight; pixY++) {
            for (int pixX = 0; pixX < realWidth; pixX++) {
                g.pixels[graphicsIndex] = getPixel(d, realX + pixX, realY + pixY);
                graphicsIndex++;
            }
        }
        d.updatePixels();

        g.updatePixels();
        g.filter(PConstants.BLUR, blurStrength);
        g.endDraw();
        d.image(g, x, y, width, height);
    }

    private static int getPixel(PApplet d, int x, int y) {
        int i = y * d.pixelWidth + x;
        if (i < 0 || i >= d.pixels.length) {
            return -1;
        }
        return d.pixels[i];
    }

}
