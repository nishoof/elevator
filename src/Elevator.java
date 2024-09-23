import processing.core.PApplet;
import processing.core.PConstants;

public class Elevator implements DrawableObject {
    
    private final int secPerFloor = 1;

    private int currentFloor;
    private boolean moving;



    /**
     * Constructs a new Elevator
     */
    public Elevator() {
        currentFloor = 0;
        moving = false;
    }



    @Override
    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Rectangle representing the elevator I guess
        d.rectMode(PConstants.CENTER);
        d.rect(d.width / 2 - 200, d.height / 2, 100, 200);
        
        // Text
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.textSize(32);
        d.fill(0);
        d.text("Elevator is currently at floor " + this.getCurrentFloor()
                , d.width / 2 - 50, d.height / 2 - 85);
        
        d.pop();           // Restore original settings
    }
    
    /**
     * Moves the elevator to the newFloor.
     * @return if the elevator is now at newFloor
     */
    public boolean goToNewFloor(int newFloor) {
        if (this.currentFloor == newFloor) return true;

        moving = true;

        System.out.println("Going to floor " + newFloor + "...");
        
        while (this.currentFloor != newFloor) {
            try {
                Thread.sleep(secPerFloor * 1000);
                if (this.currentFloor < newFloor) this.currentFloor++;
                else this.currentFloor--;
            } catch (InterruptedException e) {
                System.err.println("goToNewFloor() was interrupted");
                return false;
            }
            System.out.println("Now at floor " + this.currentFloor);
        }
        
        moving = false;

        System.out.println("Reached floor " + newFloor);

        return true;
    }



    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public boolean getMoving() {
        return this.moving;
    }

}
