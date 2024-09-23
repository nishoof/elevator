import java.util.ArrayList;
// import java.util.Scanner;

import processing.core.PApplet;

public class Main extends PApplet {
    
    private final int X_RATIO = 16;
    private final int Y_RATIO = 9;
    private final int WINDOW_SIZE = 60;

    private ArrayList<DrawableObject> drawableObjects;
    private ArrayList<ClickableObject> clickableObjects;
    // private Elevator elevator;
    // private ElevatorUser user;
    // private ElevatorButton button;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        this.size(WINDOW_SIZE * X_RATIO, WINDOW_SIZE * Y_RATIO);
    }

    public void setup() {
        // ElevatorUser user = new ElevatorUser();
        Elevator elevator = new Elevator();
        ElevatorButton button1 = new ElevatorButton(this.width / 2, this.height / 2 + 25, "1");
        
        drawableObjects = new ArrayList<>();
        clickableObjects = new ArrayList<>();

        drawableObjects.add(elevator);
        drawableObjects.add(button1);

        clickableObjects.add(button1);
        
        // new Thread(() -> {
        //     while (true) {
        //         user.promptForInput(elevator, new Scanner(System.in));
        //     }
        // }).start();
    }

    public void draw() {        
        background(200);

        for (DrawableObject dObject : drawableObjects) {
            dObject.draw(this);
        }

        // if (clickableObjects.get(0).contains(mouseX, mouseY)) System.out.println("TRUE");
        // else System.out.println("FALSE");
    }

    public void mousePressed() {
        for (ClickableObject cObject : clickableObjects) {
            if (cObject.contains(mouseX, mouseY)) cObject.clicked();
        }
    }

}
