package Main;

import java.awt.Point;

import java.util.ArrayList;

import Elements.Button.Button;
import Elements.Button.ButtonListener;
import Screens.Game;
import Screens.LevelSelect;
import Screens.Menu;
import Screens.Screen;
import Screens.Upgrades;
import processing.core.PApplet;
import processing.core.PConstants;

public class Main extends PApplet implements ButtonListener {
    
    // Window Constants
    public static final int X_RATIO = 16;
    public static final int Y_RATIO = 9;
    public static final int WINDOW_SIZE = 60;
    public static final int WINDOW_WIDTH = WINDOW_SIZE * X_RATIO;
    public static final int WINDOW_HEIGHT = WINDOW_SIZE * Y_RATIO;

    // Screen IDs
    public static final int MENU = 0;
    public static final int LEVEL_SELECT = 1;
    public static final int LEVEL_1 = 2;
    public static final int LEVEL_2 = 3;
    public static final int LEVEL_3 = 4;
    public static final int UPGRADES = 5;

    // Instance of main
    private static Main instance;

    // Screen
    private int currentScreen;
    private ArrayList<Screen> screens;
    private Game currGame;

    // Menu Play Button
    private Button menuPlayButton;

    public Main() {
        if (instance != null) throw new IllegalStateException("There can only be one instance of Main");
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

        // Fonts
        FontHolder.setRegular(createFont("GeistMono-Regular.otf", 128));
        FontHolder.setMedium(createFont("GeistMono-Medium.otf", 128));
        textMode(PConstants.MODEL);
        
        // Screens
        currentScreen = MENU;
        screens = new ArrayList<>();
        screens.add(new Menu());
        screens.add(new LevelSelect());
        screens.add(new Game(1, 3, 2000, 3500));
        screens.add(new Game(1, 7, 1000, 3500));
        screens.add(new Game(2, 10, 500, 4000));
        screens.add(new Upgrades());
        currGame = null;

        // Menu Play Button
        menuPlayButton = ((Menu)(screens.get(MENU))).getPlayButton();
        menuPlayButton.addListener(this);
    }

    @Override
    public void draw() {
        windowRatio(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Background (clears screen too)
        background(255);

        // Draw current screen
        screens.get(currentScreen).draw(this);
    }

    @Override
    public void mousePressed() {
        Point mouse = getScaledMouse(this);

        screens.get(currentScreen).mousePressed(mouse.x, mouse.y);
    }

    @Override
    public void keyPressed() {
        if (key == 'b') {               // toggle upgrades screen
            if (currentScreen > LEVEL_SELECT) toggleUpgradesScreen();
        }

        screens.get(currentScreen).keyPressed(key);
    }

    @Override
    // Implementing ButtonListener method
    public void onClick(Button button) {
        // If play button was pressed, switch to game screen
        if (button.equals(menuPlayButton)) {
            currentScreen = LEVEL_SELECT;
        }
    }

    public void switchScreen(int screen) {
        currentScreen = screen;
    }

    public void switchScreen(Screen screen) {
        switchScreen(screens.indexOf(screen));
    }

    public void startLevel(int level) {
        // Figure out which screen corresponds to the level we want
        int screen = LEVEL_SELECT + level;
        Game game = (Game)(screens.get(screen));
        
        // Start the game for the level
        game.startTime();

        // Set the current game
        currGame = game;
        
        // Switch to the screen
        switchScreen(screen);
    }

    public void toggleUpgradesScreen() {
        if (currGame == null) throw new IllegalStateException("Cannot toggle upgrades screen outside of a game");

        if (currentScreen == UPGRADES) switchScreen(currGame);
        else switchScreen(UPGRADES);
    }

    public static Main getInstance() {
        return instance;
    }

    public static Point getScaledMouse(PApplet d) {
        int scaledMouseX = (int)(1.0 * d.mouseX / d.width * WINDOW_WIDTH);
        int scaledMouseY = (int)(1.0 * d.mouseY / d.height * WINDOW_HEIGHT);
        
        return new Point(scaledMouseX, scaledMouseY);
    }

    public static void drawGameTitle(PApplet d) {
        d.push();          // Save original settings        

        d.strokeWeight(0);
        d.textFont(FontHolder.getRegular());
        d.fill(0);
        d.rect(280, 25, 400, 50);      // outer black rect
        d.textSize(32);
        d.fill(255);
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.text("Elevator Simulator", 285, 50);
        d.rect(645, 35, 20, 30);      // small white rectangle symbol

        d.pop();           // Restore original settings
    }

}
