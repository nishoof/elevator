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

    // Screen Switching Buttons
    private Button menuPlayButton;
    private Button returnToMenuButton;

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

        // Load all resources (images and fonts)
        DataHolder.init(this);

        // Return to Menu Button
        returnToMenuButton = new Button(307, 255, 346, 70);
        returnToMenuButton.setTextSize(28);
        returnToMenuButton.setText("Return to Menu!");
        returnToMenuButton.addListener(this);

        // Screens
        currentScreen = MENU;
        screens = new ArrayList<>();
        screens.add(new Menu());
        screens.add(new LevelSelect());

        int[][] waves = new int[][] {{3, 3000, 3000}, {5, 2500, 2500}, {10, 750, 1250}};
        screens.add(null);
        screens.add(null);
        screens.add(null);
        // screens.add(new Game(1, 3, waves));
        // screens.add(new Game(1, 7, waves));
        // screens.add(new Game(2, 10, waves));

        screens.add(new Upgrades());
        currGame = null;

        // Menu Play Button
        menuPlayButton = ((Menu)(screens.get(MENU))).getPlayButton();
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
        Point mouse = getScaledMouse(this);

        System.out.println(mouse);

        screens.get(currentScreen).mousePressed(mouse.x, mouse.y);
    }

    @Override
    public void keyPressed() {
        if (key == 'b') {               // toggle upgrades screen
            if (currentScreen == UPGRADES) {
                toggleUpgradesScreen();
                return;
            }

            Screen screen = screens.get(currentScreen);
            if (!(screen instanceof Game)) return;
            if (((Game)screen).getGameOver()) return;

            toggleUpgradesScreen();
            return;
        }

        screens.get(currentScreen).keyPressed(key);
    }

    @Override
    // Implementing ButtonListener method
    public void onClick(Button button) {
        if (button == menuPlayButton) {
            currentScreen = LEVEL_SELECT;
        } else if (button == returnToMenuButton) {
            currentScreen = MENU;
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
        // Figure out which screen corresponds to the level we want
        int screenIndex = LEVEL_SELECT + level;

        // Make the new game
        int[][] waves = new int[][] {{3, 3000, 3000}, {5, 2500, 2500}, {10, 750, 1250}};
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
        screens.set(screenIndex, game);
        
        // Start the game for the level
        game.startTime();

        // Set the current game
        currGame = game;
        
        // Switch to the screen
        switchScreen(screenIndex);
    }

    public void toggleUpgradesScreen() {
        if (currGame == null) throw new IllegalStateException("Cannot toggle upgrades screen outside of a game");

        if (currentScreen == UPGRADES) switchScreen(currGame);
        else switchScreen(UPGRADES);
    }

    public Button getReturnToMenuButton() {
        return returnToMenuButton;
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
        d.textFont(DataHolder.getRegularFont());
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
