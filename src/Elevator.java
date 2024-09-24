// import java.util.Arrays;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PConstants;

public class Elevator implements DrawableObject {
    
    private final int secPerFloor = 1;
    private final int secDoorsOpen = 3;                 // how long the door stays open for
    private final double secDoorsDelay = 0.5;           // the delay between elevator stop -> door open or door open -> elevator move
    private final double secDoorsToOpen = 0.5;          // the time it takes for the door to open/close (animation time)

    private int currentFloor;
    private int highestFloor;
    private int lowestFloor;
    
    private int floorsInQueue;
    private boolean[] queuedFloors;
    private boolean moving;
    private int doorsOpenPercent;
    
    private int x;
    private int y;
    private int width;
    private int shaftHeight;
    private int cabinHeight;
    
    private ArrayList<ElevatorButton> buttons;



    /**
     * Constructs a new Elevator
     */
    public Elevator(int x, int y, int width, int shaftHeight) {        
        currentFloor = 1;
        highestFloor = 5;
        lowestFloor = 1;

        queuedFloors = new boolean[highestFloor - lowestFloor + 1];
        
        moving = false;
        doorsOpenPercent = 0;

        this.x = x;
        this.y = y;
        this.width = width;
        this.shaftHeight = shaftHeight;
        this.cabinHeight = shaftHeight / queuedFloors.length;

        buttons = new ArrayList<>();

        for (int i = lowestFloor; i <= highestFloor; i++) {
            ElevatorButton button = new ElevatorButton(x + 85 + i * 100, y + 25, i);
            buttons.add(button);
        }
    }



    @Override
    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Elevator Shaft
        d.rectMode(PConstants.CORNER);
        d.fill(255);
        d.rect(x - width / 2, y - shaftHeight / 2, width, shaftHeight);

        // Elevator Cabin
        if (doorsOpenPercent == 0) {
            d.fill(0);
            d.rect(x - width / 2, y + shaftHeight / 2 - cabinHeight - (currentFloor - lowestFloor) * cabinHeight, width, cabinHeight);
        } else {
            // Outer box
            d.fill(200);
            d.rect(x - width / 2, y + shaftHeight / 2 - cabinHeight - (currentFloor - lowestFloor) * cabinHeight, width, cabinHeight);

            // Doors
            int doorWidth = width / 2 * (100 - doorsOpenPercent) / 100;
            d.fill(0);
            d.rect(x - width / 2, y + shaftHeight / 2 - cabinHeight - (currentFloor - lowestFloor) * cabinHeight, doorWidth, cabinHeight);
            d.rect(x + width / 2 - doorWidth, y + shaftHeight / 2 - cabinHeight - (currentFloor - lowestFloor) * cabinHeight, doorWidth, cabinHeight);
        }
        
        // Text
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.textSize(32);
        d.fill(0);
        d.text("Elevator is currently at floor " + this.getCurrentFloor()
        , d.width / 2 - 50, d.height / 2 - 85);
        
        // Buttons
        for (ElevatorButton button : buttons) {
            button.setOn(queuedFloors[button.getFloorNum() - lowestFloor]);
            button.draw(d);
        }

        // Testing Purposes, show queue
        d.textAlign(PConstants.LEFT, PConstants.TOP);
        d.textSize(20);
        d.text(Arrays.toString(queuedFloors) + "\n" + doorsOpenPercent,
                0, 0);
        
        d.pop();           // Restore original settings
    }

    public void mousePressed(PApplet d) {
        for (ElevatorButton button : buttons) {
            if (button.contains(d.mouseX, d.mouseY)) {
                new Thread(() -> {
                    this.addFloorToQueue(button.getFloorNum());
                }).start();
            }
        }
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
        final int SMOOTHNESS = 25;      // 100 must be divisible by this number for proper animation

        // Delay
        try {
            Thread.sleep((long)(secDoorsDelay * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Open doors
        System.out.println("OPENING DOOR");
        doorsOpenPercent = 0;
        while (doorsOpenPercent < 100) {
            try {
                Thread.sleep((long)(secDoorsToOpen * 1000 / SMOOTHNESS));
                doorsOpenPercent += 100 / SMOOTHNESS;
                System.out.println(doorsOpenPercent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Hold doors open
        try {
            Thread.sleep((long)(secDoorsOpen * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Close doors
        System.out.println("CLOSING DOOR");
        while (doorsOpenPercent > 0) {
            try {
                Thread.sleep((long)(secDoorsToOpen * 1000 / SMOOTHNESS));
                doorsOpenPercent -= 100 / SMOOTHNESS;
                System.out.println(doorsOpenPercent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Delay
        try {
            Thread.sleep((long)(secDoorsDelay * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
