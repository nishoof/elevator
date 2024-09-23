import java.util.ArrayList;

import processing.core.PApplet;

public class Main extends PApplet {
    
    private final int X_RATIO = 16;
    private final int Y_RATIO = 9;
    private final int WINDOW_SIZE = 60;

    private ArrayList<DrawableObject> drawableObjects;
    private ArrayList<ElevatorButton> buttons;

    private Elevator elevator;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        this.size(WINDOW_SIZE * X_RATIO, WINDOW_SIZE * Y_RATIO);
    }

    public void setup() {
        drawableObjects = new ArrayList<>();
        buttons = new ArrayList<>();
        
        elevator = new Elevator();
        drawableObjects.add(elevator);

        for (int i = 0; i < 5; i++) {
            ElevatorButton button = new ElevatorButton(this.width / 2 + i * 100, this.height / 2 + 25, i + 1);
            drawableObjects.add(button);
            buttons.add(button);
        }
        
    }

    public void draw() {        
        background(200);

        for (DrawableObject dObject : drawableObjects) {
            dObject.draw(this);
        }
    }

    public void mousePressed() {
        for (ElevatorButton button : buttons) {
            if (button.contains(mouseX, mouseY)) {
                new Thread(() -> {
                    elevator.goToNewFloor(button.getFloorNum());
                }).start();
            }
        }
    }

}
