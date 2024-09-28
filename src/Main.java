import java.util.ArrayList;

import processing.core.PApplet;

public class Main extends PApplet {
    
    private final int X_RATIO = 16;
    private final int Y_RATIO = 9;
    private final int WINDOW_SIZE = 60;

    private ArrayList<Drawable> drawables;
    private ArrayList<Clickable> clickables;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        this.size(WINDOW_SIZE * X_RATIO, WINDOW_SIZE * Y_RATIO);
    }

    public void setup() {
        Elevator elevator1 = new Elevator(50, 200, 400, 200, 10);
        Elevator elevator2 = new Elevator(500, 200, 400, 200, 10);
        
        drawables = new ArrayList<>();
        drawables.add(elevator1);
        drawables.add(elevator2);

        clickables = new ArrayList<>();
        clickables.add(elevator1);
        clickables.add(elevator2);
    }

    public void draw() {
        background(200);

        for (Drawable element : drawables) {
            element.draw(this);
        }
    }

    public void mousePressed() {
        for (Clickable element : clickables) {
            element.mousePressed(this);
        }
    }

}
