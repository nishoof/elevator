package Elements;

public class Person {

    private int currentFloor;
    private int desiredFloor;

    public Person(int currentFloor, int desiredFloor) {
        this.currentFloor = currentFloor;
        this.desiredFloor = desiredFloor;
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

}
