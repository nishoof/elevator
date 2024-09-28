import java.util.ArrayList;

import processing.core.PApplet;

public class Main extends PApplet {
    
    private final int X_RATIO = 16;
    private final int Y_RATIO = 9;
    private final int WINDOW_SIZE = 60;

    private ArrayList<DrawableObject> drawableObjects;

    private Elevator elevator;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        this.size(WINDOW_SIZE * X_RATIO, WINDOW_SIZE * Y_RATIO);
    }

    public void setup() {
        elevator = new Elevator(50, 200, 400, 200, 10);
        
        drawableObjects = new ArrayList<>();
        drawableObjects.add(elevator);
    }

    public void draw() {
        background(200);

        for (DrawableObject dObject : drawableObjects) {
            dObject.draw(this);
        }
    }

    public void mousePressed() {
        elevator.mousePressed(this);
    }

}
