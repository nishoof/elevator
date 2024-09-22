public class Elevator {
    
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
