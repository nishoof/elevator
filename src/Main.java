import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;

public class Main extends PApplet {
    
    private final int X_RATIO = 16;
    private final int Y_RATIO = 9;
    private final int WINDOW_SIZE = 60;

    private ArrayList<Drawable> drawables;
    private ArrayList<Clickable> clickables;

    private HashMap<Character, Elevator> charToElevatorMap;
    private Elevator selectedElevator;;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        this.size(WINDOW_SIZE * X_RATIO, WINDOW_SIZE * Y_RATIO);
    }

    public void setup() {
        Elevator elevator1 = new Elevator(50, 50, 400, 200, 9);
        Elevator elevator2 = new Elevator(500, 300, 400, 200, 9);
        
        drawables = new ArrayList<>();
        drawables.add(elevator1);
        drawables.add(elevator2);

        clickables = new ArrayList<>();
        clickables.add(elevator1);
        clickables.add(elevator2);

        charToElevatorMap = new HashMap<>();
        charToElevatorMap.put('q', elevator1);
        charToElevatorMap.put('w', elevator2);

        selectedElevator = null;
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

        // System.out.println(color(255, 0, 0));
    }

    public void keyPressed() {
        // Make the key lowercase to make it work even if the user did a capital for some reason
        char key = Character.toLowerCase(this.key);

        if (key >= 'a' && key <= 'z') {                   // keyIsALetter
            // Figure out what elevator is paired to the key that was pressed
            Elevator elevator = charToElevatorMap.get(key);

            // Ignore keys that aren't paired to an elevator
            if (elevator == null) return;

            // Select the elevator
            if (selectedElevator != null) selectedElevator.setHighlighted(false);
            selectedElevator = elevator;
            selectedElevator.setHighlighted(true);
            
            System.out.println("Selected elevator " + elevator);
        } else if (key >= '1' && key <= '9') {            // keyIsANumber
            // If there is no elevator selected already, then there's nothing we can do
            if (selectedElevator == null) return;

            System.out.println((int)(key - '0'));

            // Otherwise, tell the elevator to go to this floor
            selectedElevator.addFloorToQueue(key - '0');
        }
    }

}
