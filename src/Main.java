import java.awt.Point;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;

public class Main extends PApplet {
    
    public static final int X_RATIO = 16;
    public static final int Y_RATIO = 9;
    public static final int WINDOW_SIZE = 60;
    public static final int WINDOW_WIDTH = WINDOW_SIZE * X_RATIO;
    public static final int WINDOW_HEIGHT = WINDOW_SIZE * Y_RATIO;

    private int currentScreen;
    private ArrayList<Screen> screens;
    /*
     * 0: Menu
     * 1: Game
     */

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
        smooth();
    }

    public void setup() {
        // Window
        windowResizable(true);
        windowTitle("Elevator Simulator");

        // Fonts
        FontHolder.setRegular(createFont("GeistMono-Regular.otf", 128));
        FontHolder.setMedium(createFont("GeistMono-Medium.otf", 128));
        textMode(PConstants.MODEL);
        
        // Screens
        currentScreen = 0;
        screens = new ArrayList<>();
        Menu menu = new Menu();
        screens.add(menu);
        screens.add(null);
    }

    public void draw() {
        windowRatio(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Background (clears screen too)
        background(255);

        // Switch screens if menu's play button was pressed
        Menu menu = (Menu)(screens.get(0));
        if (currentScreen == 0 && menu.playButtonPressed()) {
            screens.set(1, new Game());
            currentScreen = 1;
        }

        // Draw current screen
        screens.get(currentScreen).draw(this);
    }

    public void mousePressed() {
        Point mouse = getScaledMouse(this);

        screens.get(currentScreen).mousePressed(mouse.x, mouse.y);
    }

    public void keyPressed() {
        screens.get(currentScreen).keyPressed(key);
    }

    public static Point getScaledMouse(PApplet d) {
        int scaledMouseX = (int)(1.0 * d.mouseX / d.width * WINDOW_WIDTH);
        int scaledMouseY = (int)(1.0 * d.mouseY / d.height * WINDOW_HEIGHT);
        
        return new Point(scaledMouseX, scaledMouseY);
    }

}
