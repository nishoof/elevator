package Elements;

import processing.core.PApplet;
import processing.core.PConstants;

public class Person {

    private int currentFloor;
    private int desiredFloor;
    private long startTime;
    private long endTime;
    private boolean timerRunning; // the timer runs while the person is waiting for the elevator
    private RemovePersonMethod removePersonMethod;

    private static final int TEXT_SIZE = 19;
    private static final float TEXT_ASCENT = 13.5078125F;

    public interface RemovePersonMethod {
        void removePerson(Person person);
    }

    /**
     * Constructs a new Person
     * 
     * @param currentFloor       the floor the person is currently on
     * @param desiredFloor       the floor the person wants to go to
     * @param duration           how long the person will wait for the elevator, in
     *                           miliseconds
     * @param removePersonMethod the method to call when the person has waited too
     *                           long. Must be a method that takes a Person as a
     *                           parameter and returns void
     */
    public Person(int currentFloor, int desiredFloor, int duration, RemovePersonMethod removePersonMethod) {
        if (currentFloor == desiredFloor)
            throw new IllegalArgumentException("The current floor and desired floor cannot be the same");
        if (duration < 0)
            throw new IllegalArgumentException("Duration cannot be negative");

        this.currentFloor = currentFloor;
        this.desiredFloor = desiredFloor;
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + duration;
        this.removePersonMethod = removePersonMethod;
        new Thread(this::startTimer).start();
    }

    public void draw(PApplet d, int x, int y) {
        d.textSize(TEXT_SIZE);
        d.textAlign(PConstants.LEFT, PConstants.CENTER);
        d.text(this.toString(), x, y);

        float percentTimeRemaining = (float) (System.currentTimeMillis() - startTime) / (endTime - startTime);
        d.ellipseMode(PConstants.CENTER);
        d.arc(x + 95, y, TEXT_ASCENT - 2, TEXT_ASCENT - 2,
                -PConstants.PI / 2 + (percentTimeRemaining * 2 * PConstants.PI), 3 * PConstants.PI / 2, PConstants.PIE);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getDesiredFloor() {
        return desiredFloor;
    }

    public void setDesiredFloor(int desiredFloor) {
        this.desiredFloor = desiredFloor;
    }

    public String toString() {
        return currentFloor + " -> " + desiredFloor;
    }

    public void cancelTimer() {
        timerRunning = false;
    }

    private void startTimer() {
        final int BUSY_WAIT = 1000;
        timerRunning = true;

        try {
            // Wait for the duration of the timer - BUSY_WAIT
            long waitTime = endTime - System.currentTimeMillis() - BUSY_WAIT;
            Thread.sleep(waitTime > 0 ? waitTime : 0);

            // Busy wait for the remaining time
            while (System.currentTimeMillis() < endTime && timerRunning) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (timerRunning && removePersonMethod != null) {
            removePersonMethod.removePerson(this);
        }

        timerRunning = false;
    }

}
