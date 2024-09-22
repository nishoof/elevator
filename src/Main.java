import java.util.Scanner;

import processing.core.PApplet;

public class Main extends PApplet {
    
    private final int X_RATIO = 16;
    private final int Y_RATIO = 9;
    private final int WINDOW_SIZE = 60;

    private Elevator elevator;
    private ElevatorUser user;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        this.size(WINDOW_SIZE * X_RATIO, WINDOW_SIZE * Y_RATIO);
    }

    public void setup() {
        elevator = new Elevator();
        user = new ElevatorUser();

        new Thread(() -> {
            while (true) {
                user.promptForInput(elevator, new Scanner(System.in));
            }
        }).start();
    }

    public void draw() {        
        background(200);

        elevator.draw(this);
    }

}
