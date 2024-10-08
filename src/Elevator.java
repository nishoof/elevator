import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;

public class Elevator {

    private final int maxFloors = 10;
    
    private final double secPerFloor = 0.6;
    private final double secDoorsOpen = 1.5;                // how long the door stays open for
    private final double secDoorsDelay = 0.5;               // the delay between elevator stop -> door open or door open -> elevator move
    private final double secDoorsToOpen = 0.25;             // the time it takes for the door to open/close (animation time)

    private final double tickWidthPercent = 0.15;
    private final int shaftButtonMargin = 40;               // margin between right bound of shaft and left bound of left most button
    private final int buttonButtonMargin = 10;              // margin between right bound of a button and left bound of the button to the right
    private final int maxfloorNumberTextSize = 25;
    
    private final Game game;

    private int currentFloor;
    private int highestFloor;
    private int lowestFloor;
    private int floorPercent;                               // used for animation

    private int floorsInQueue;
    private boolean[] queuedFloors;
    private int status;
    private int doorsOpenPercent;                           // used for animation
    private boolean doorsInAnimation;                       // used for animation. If the doors are in the process of opening/closing (including the delay before & after), this is true

    private int x;
    private int y;
    private int boundaryHeight;
    private int shaftWidth;
    private int shaftHeight;
    private int cabinHeight;

    private boolean highlighted;

    private ArrayList<ElevatorButton> buttons;

    private ArrayList<Person> peopleInElevator;

    /**
     * Constructs a new Elevator
     */
    public Elevator(int x, int y, int width, int height, int numFloors, Game game) {
        this.x = x;
        this.y = y;
        this.shaftWidth = width / 4;
        this.boundaryHeight = height;
        this.shaftHeight = boundaryHeight - (boundaryHeight % numFloors);
        this.cabinHeight = shaftHeight / numFloors;

        this.currentFloor = 1;
        this.lowestFloor = 1;
        this.highestFloor = numFloors;

        this.queuedFloors = new boolean[numFloors];
        
        this.status = 0;
        this.doorsOpenPercent = 0;
        this.doorsInAnimation = false;
        
        buttons = new ArrayList<>();

        int size = ((shaftWidth * 3) - shaftButtonMargin - (4 * buttonButtonMargin)) / 10;
        int numButtonsPerRow = 5;
        int leftMostButtonCenter = x + shaftWidth + shaftButtonMargin + size;
        int topMostButtonCenter = y + size;
        int numButtonsCreated = 0;

        highlighted = false;

        for (int i = 0; i <= numFloors / numButtonsPerRow; i++) {
            for (int j = 0; j < 5; j++) {
                if (numButtonsCreated == queuedFloors.length) break;

                int buttonX = leftMostButtonCenter + (j * (buttonButtonMargin + size * 2));
                int buttonY = topMostButtonCenter + (i * (buttonButtonMargin + size * 2));

                ElevatorButton button = new ElevatorButton(buttonX, buttonY, i*5 + j + 1, size, 10);
                buttons.add(button);

                numButtonsCreated++;
            }
        }

        peopleInElevator = new ArrayList<>();

        this.game = game;
    }

    public void draw(PApplet d) {
        d.push();          // Save original settings

        int strokeWeight = 3;
        
        // Boundary
        // d.rectMode(PConstants.CORNER);
        // d.fill(255);
        // d.strokeWeight(3);
        // d.rect(x, y, shaftWidth * 4, shaftHeight);

        // Elevator Shaft
        d.rectMode(PConstants.CORNER);
        d.fill(255);
        d.stroke(highlighted ? -65536 : 0);
        d.strokeWeight(strokeWeight);
        d.rect(x, y, shaftWidth, shaftHeight, 8);
        
        // Elevator Floor Ticks Marks
        d.push();
        int grey = 125;
        int tickWidth = (int)(shaftWidth * tickWidthPercent);
        int lowestTickY = y + shaftHeight - cabinHeight;
        for (int i = 0; i < highestFloor - lowestFloor; i++) {
            int tickY = lowestTickY - (i * cabinHeight);
            d.strokeWeight(1);
            d.stroke(grey);
            d.line(x + strokeWeight/2 + 1, tickY, x + tickWidth, tickY);
        }
        
        // Elevator Floor Numbers
        int floorHeight = shaftHeight / (highestFloor - lowestFloor + 1) - 6;
        int floorNumberTextSize = (floorHeight > maxfloorNumberTextSize) ? maxfloorNumberTextSize : floorHeight;
        d.textFont(FontHolder.getRegular());
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.fill(grey);
        d.textSize(floorNumberTextSize);
        d.text(highestFloor, x + shaftWidth / 2, y + strokeWeight/2+1 + floorHeight / 2);
        d.pop();
        
        // Elevator Cabin
        int bottomRectCornerRounding = (currentFloor == lowestFloor && floorPercent == 0) ? 8 : 0;
        int topRectCornerRounding = (currentFloor == highestFloor && floorPercent == 0) ? 8 : 0;
        if (doorsOpenPercent == 0) {
            // If the door is 100% closed, then we just need to draw a rect
            d.fill(0);
            d.rect(x, y + shaftHeight - cabinHeight - (currentFloor - lowestFloor) * cabinHeight - (floorPercent * cabinHeight / 100), shaftWidth, cabinHeight, topRectCornerRounding, topRectCornerRounding, bottomRectCornerRounding, bottomRectCornerRounding);
        } else {
            // Otherwise, we need to draw in the inside of the elevator and the doors seperately (3 rects)
            
            // Inside of elevator
            d.fill(220);
            d.rect(x, y + shaftHeight - cabinHeight - (currentFloor - lowestFloor) * cabinHeight, shaftWidth, cabinHeight, topRectCornerRounding, topRectCornerRounding, bottomRectCornerRounding, bottomRectCornerRounding);

            // Doors
            int doorWidth = shaftWidth / 2 * (100 - doorsOpenPercent) / 100;
            if (doorWidth != 0) {
                d.fill(0);
                d.rect(x, y + shaftHeight - cabinHeight - (currentFloor - lowestFloor) * cabinHeight, doorWidth, cabinHeight, topRectCornerRounding, topRectCornerRounding, bottomRectCornerRounding, bottomRectCornerRounding);
                d.rect(x + shaftWidth - doorWidth, y + shaftHeight - cabinHeight - (currentFloor - lowestFloor) * cabinHeight, doorWidth, cabinHeight, topRectCornerRounding, topRectCornerRounding, bottomRectCornerRounding, bottomRectCornerRounding);
            }
        }

        // Buttons
        for (ElevatorButton button : buttons) {
            button.setOn(queuedFloors[button.getFloorNum() - lowestFloor]);
            button.draw(d);
        }

        // People in the elevator
        StringBuilder peopleInElevatorStr = new StringBuilder();
        for (int i = 0; i < peopleInElevator.size(); i++) {
            peopleInElevatorStr.append(peopleInElevator.get(i).getDesiredFloor());
            if (i != peopleInElevator.size() - 1) peopleInElevatorStr.append(", ");
        }
        d.textAlign(PConstants.LEFT, PConstants.BOTTOM);
        d.fill(0);
        d.textSize(20);
        String peopleInElevatorDisplayStr = null;
        if (peopleInElevator.size() == 0) {
            peopleInElevatorDisplayStr = "No one in elevator";
        } else {
            peopleInElevatorDisplayStr = peopleInElevatorStr.toString();
        }
        d.text(peopleInElevatorDisplayStr, x + shaftWidth + shaftButtonMargin, y - 5, shaftWidth * 3 - shaftButtonMargin, shaftHeight);
        
        // Testing Purposes, show queue
        // d.textAlign(PConstants.LEFT, PConstants.TOP);
        // d.textSize(20);
        // d.text("Testing: " + "\n" + Arrays.toString(queuedFloors) + "\n" + "doorsOpenPercent: " + doorsOpenPercent + "\n" + "floorPercent: " + floorPercent + "\n" + "status: " + status,
        //         600, 400);
        
        d.pop();           // Restore original settings
    }

    public void mousePressed(int mouseX, int mouseY) {
        // System.out.println(mouseX + "\t" + mouseY);
        for (ElevatorButton button : buttons) {
            if (button.contains(mouseX, mouseY)) {
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
        
        // If we are already at the floor, just open the doors and return
        if (this.currentFloor == newFloor) {
            // If the elevator isn't moving and doors are closed, then simply open the doors without delay
            if (status == 0 && doorsOpenPercent == 0) {
                reachedFloor(false);
            }
            return true;
        }

        // If the floor we wanted to go to is already in the queue, then there's nothing to do
        if (queuedFloors[newFloor - lowestFloor]) return true;

        // If we got here, then we have a floor to add to the queue...
        queuedFloors[newFloor - lowestFloor] = true;
        floorsInQueue++;

        // If the elevator is stationary, we need to get it to start moving. Otherwise, it will get there on its own
        if (status == 0) new Thread(() -> move(newFloor > currentFloor ? 1 : -1)).start();

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
     * Sets whether this Elevator is highlighted or not. When this Elevator is highlighted, the shaft is drawn with a red stroke
     * @param highlighted new value for if this Elevator is highlighted or not
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * Returns an ArrayList of Persons in this elevator
     * @return an ArrayList of Persons in this elevator
     */
    public ArrayList<Person> getPeopleInElevator() {
        return peopleInElevator;
    }

    /**
     * Makes the elevator move. Should be called in its own thread. 
     * @param direction Either 1 (going up) or -1 (going down)
     */
    private void move(int direction) {
        while (doorsInAnimation) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        if (direction != 1 && direction != -1) throw new IllegalArgumentException("Invalid direction \"" + direction + "\", must be -1 or 1");
        
        status = direction;
        
        // If there are floors in the queue, keep the elevator moving until all floors have been reached and removed from queue
        while (floorsInQueue > 0) {
            // First count the number of floors in the current direction

            int floorsInCurrDirection = 0;              // TODO: could probably make this a boolean

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

            animateMovingOneFloor(direction);

            // If this was a queued floor (floor we need to stop at), remove it from the queue then call reachedFloor() since we reached the floor

            if (queuedFloors[currentFloor - lowestFloor]) {
                queuedFloors[currentFloor - lowestFloor] = false;
                floorsInQueue--;
                reachedFloor(true);
            }
        }

        status = 0;
    }

    private void animateMovingOneFloor(int direction) {
        final int SMOOTHNESS = 25;      // 100 must be divisible by this number for proper animation
        
        floorPercent = 0;

        // Animate the floor moving
        while (Math.abs(floorPercent) < 100) {
            try {
                Thread.sleep((long)(secPerFloor * 1000 / SMOOTHNESS));
                floorPercent += 100 / SMOOTHNESS * direction;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // After the wait, we are at the next floor. Update currentFloor appropriately
        currentFloor += direction;

        floorPercent = 0;
    }

    /**
     * Called when we reach a floor so that we can open/close the doors, handle people going in/out of elevators, and give points if needed
     */
    private void reachedFloor(boolean delayBeforeOpeningDoors) {
        final int SMOOTHNESS = 25;      // 100 must be divisible by this number for proper animation
        doorsInAnimation = true;

        // Delay
        if (delayBeforeOpeningDoors) {
            try {
                Thread.sleep((long)(secDoorsDelay * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Open doors
        doorsOpenPercent = 0;
        while (doorsOpenPercent < 100) {
            try {
                Thread.sleep((long)(secDoorsToOpen * 1000 / SMOOTHNESS));
                doorsOpenPercent += 100 / SMOOTHNESS;
                // System.out.println(doorsOpenPercent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Take the people out the elevator if they at the right floor and give points
        int n1 = 0;      // number of people removed from the elevator
        for (int i = 0; i < peopleInElevator.size() + n1; i++) {
            Person person = peopleInElevator.get(i - n1);
            if (person.getDesiredFloor() != this.getCurrentFloor()) continue;
            n1++;
            peopleInElevator.remove(person);
            game.rewardPoints();
            System.out.println("Person from floor " + person.getCurrentFloor() + " delivered to " + person.getDesiredFloor());
        }

        // Bring people into the elevator
        int n2 = 0;      // number of people removed from the line
        ArrayList<Person> peopleInLine = game.getPeopleInLine();
        for (int i = 0; i < peopleInLine.size() + n2; i++) {
            Person person = peopleInLine.get(i - n2);
            if (person.getCurrentFloor() != this.getCurrentFloor()) continue;
            n2++;
            peopleInLine.remove(person);
            peopleInElevator.add(person);
            System.out.println("Person from floor " + person.getCurrentFloor() + " added to elevator");
        }

        // Hold doors open
        try {
            Thread.sleep((long)(secDoorsOpen * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Close doors
        while (doorsOpenPercent > 0) {
            try {
                Thread.sleep((long)(secDoorsToOpen * 1000 / SMOOTHNESS));
                doorsOpenPercent -= 100 / SMOOTHNESS;
                // System.out.println(doorsOpenPercent);
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

        doorsInAnimation = false;
    }

    /**
     * Adds a floor to the elevator. This will add a button to the elevator for the new floor and update the elevator's fields accordingly
     */
    public void addFloor() {
        // If we are already at the max number of floors, can't add more. Throw an exception
        if (highestFloor == maxFloors) throw new IllegalStateException("Cannot add more floors, already at max");

        // Update queuedFloors field while preserving the old values
        boolean[] newQueuedFloors = new boolean[this.queuedFloors.length + 1];

        for (int i = 0; i < this.queuedFloors.length; i++) {
            newQueuedFloors[i] = this.queuedFloors[i];
        }

        this.queuedFloors = newQueuedFloors;

        // Update other fields
        this.highestFloor++;
        int numFloors = highestFloor - lowestFloor + 1;
        this.shaftHeight = boundaryHeight - shaftHeight % numFloors;
        this.cabinHeight = shaftHeight / numFloors;

        // Add a button
        int numButtonsPerRow = 5;
        int row = numFloors / (numButtonsPerRow + 1);
        int col = numFloors - (row * numButtonsPerRow) - 1;

        int size = ((shaftWidth * 3) - shaftButtonMargin - (4 * buttonButtonMargin)) / 10;
        int leftMostButtonCenter = x + shaftWidth + shaftButtonMargin + size;
        int topMostButtonCenter = y + size;

        int buttonX = leftMostButtonCenter + (col * (buttonButtonMargin + size * 2));
        int buttonY = topMostButtonCenter + (row * (buttonButtonMargin + size * 2));

        ElevatorButton button = new ElevatorButton(buttonX, buttonY, highestFloor, size, 10);
        buttons.add(button);
    }

}
