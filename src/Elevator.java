// import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PConstants;

public class Elevator implements DrawableObject {
    
    private final int secPerFloor = 1;
    private final int secDoorsOpen = 3;
    private final double secDoorsDelay = 0.25;

    private int currentFloor;
    private int highestFloor;
    private int lowestFloor;
    
    private boolean[] queuedFloors;
    
    private int floorsInQueue;
    private boolean moving;
    private boolean doorsOpen;



    /**
     * Constructs a new Elevator
     */
    public Elevator() {
        currentFloor = 1;
        highestFloor = 5;
        lowestFloor = 1;

        queuedFloors = new boolean[highestFloor - lowestFloor + 1];
        
        moving = false;
        doorsOpen = false;
    }



    @Override
    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Rectangle representing the elevator I guess
        d.rectMode(PConstants.CENTER);
        d.fill(doorsOpen ? 170 : 255);
        d.rect(d.width / 2 - 200, d.height / 2, 100, 200);
        
        // Text
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.textSize(32);
        d.fill(0);
        d.text("Elevator is currently at floor " + this.getCurrentFloor()
        , d.width / 2 - 50, d.height / 2 - 85);
        
        // Testing Purposes, show queue
        // d.textSize(20);
        // d.text(Arrays.toString(queuedFloors)
        //         , d.width / 2 - 50, d.height / 2 - 125);
        
        d.pop();           // Restore original settings
    }
    
    /**
     * Adds newFloor to the queue for this elevator so that it will eventually go to that floor. Will start the elevator up if it's not already moving.
     * @param newFloor The new floor number (NOT INDEX) to add to the queue
     * @return if newFloor was added to the queue successfully or if the elevator is already at newFloor
     */
    public boolean addFloorToQueue(int newFloor) {
        // Check the input to make sure it's good before proceeding
        if (newFloor < lowestFloor || newFloor > highestFloor) throw new IllegalArgumentException("Floor " + newFloor + " is out of range");
        
        // If we are already at the floor we wanted to go to, then there's nothing to do
        if (this.currentFloor == newFloor) return true;

        // If the floor we wanted to go to is already in the queue, then there's nothing to do
        if (queuedFloors[newFloor - lowestFloor]) return true;

        // If we got here, then we have a floor to add to the queue...
        queuedFloors[newFloor - lowestFloor] = true;
        floorsInQueue++;

        // If the elevator is stationary, we need to get it to start moving. Otherwise, it will get there on its own
        if (!moving) new Thread(() -> move(newFloor > currentFloor ? 1 : -1)).start();

        return true;
    }

    /**
     * Returns the current floor in displayable format (won't be 0 if on the first floor, but rather the floor number)
     * @return the current floor
     */
    public int getCurrentFloor() {
        return this.currentFloor;
    }



    /**
     * Makes the elevator move. Should be called in its own thread. 
     * @param direction Either 1 (going up) or -1 (going down)
     */
    private void move(int direction) {
        if (direction != 1 && direction != -1) throw new IllegalArgumentException("Invalid direction \"" + direction + "\", must be -1 or 1");
        
        moving = true;
        
        // If there are floors in the queue, keep the elevator moving until all floors have been reached and removed from queue
        while (floorsInQueue > 0) {
            System.out.println("floors in queue: " + floorsInQueue);

            // First count the number of floors in the current direction

            int floorsInCurrDirection = 0;

            if (direction == 1) {
                for (int i = currentFloor - lowestFloor; i < highestFloor - lowestFloor + 1; i++) {
                    if (queuedFloors[i]) floorsInCurrDirection++;
                }
            } else {
                for (int i = currentFloor - lowestFloor; i >= 0; i--) {
                    if (queuedFloors[i]) floorsInCurrDirection++;
                }
            }

            // If the floors in the current direction are 0, that means there are floors in the other direction that we need to hit so we call this method again in its own thread but going in the other direction

            if (floorsInCurrDirection == 0) {
                new Thread(() -> move(direction * -1)).start();
                return;
            }

            // If we got to this point, that means there are floors in the current direction that we need to hit.
            // So we start moving towards them. First call Thread.sleep to simulate the time it takes for an elevator to go up

            try {
                Thread.sleep(secPerFloor * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // After the wait, we are at the next floor. Update currentFloor appropriately

            currentFloor += direction;

            // If this was a queued floor (floor we need to stop at), remove it from the queue then call reachedFloor() since we reached the floor

            if (queuedFloors[currentFloor - lowestFloor]) {
                queuedFloors[currentFloor - lowestFloor] = false;
                floorsInQueue--;
                reachedFloor();
            }
        }

        moving = false;
    }

    /**
     * Called when we reach a floor so that we can open the doors
     */
    private void reachedFloor() {
        System.out.println("Reached floor " + currentFloor);

        try {
            Thread.sleep((long)(secDoorsDelay * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        doorsOpen = true;

        try {
            Thread.sleep(secDoorsOpen * 1000 + (long)(secDoorsDelay * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        doorsOpen = false;
    }

}
