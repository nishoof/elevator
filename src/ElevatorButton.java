import processing.core.PApplet;
import processing.core.PConstants;

public class ElevatorButton {
    
    private int x;
    private int y;
    private int floorNum;
    private String text;
    private long radius;
    private boolean on;
    private int onOutlineColor;



    /**
     * Constructs a new ElevatorButton
     * @param x x-coordinate of the center of this ElevatorButton
     * @param y y-coordinate of the center of this ElevatorButton
     * @param floorNum floor number of this ElevatorButton
     */
    public ElevatorButton(int x, int y, int floorNum) {
        this.x = x;
        this.y = y;
        this.floorNum = floorNum;
        this.text = String.valueOf(floorNum);
        this.radius = 32;
        this.on = false;
        this.onOutlineColor = -14357508;
    }

    /**
     * Constructs a new ElevatorButton
     * @param x x-coordinate of the center of this ElevatorButton
     * @param y y-coordinate of the center of this ElevatorButton
     * @param floorNum floor number of this ElevatorButton
     * @param radius the radius of the UI element representing this ElevatorButton
     */
    public ElevatorButton(int x, int y, int floorNum, long radius) {
        this.x = x;
        this.y = y;
        this.floorNum = floorNum;
        this.text = String.valueOf(floorNum);
        this.radius = radius;
        this.on = false;
        this.onOutlineColor = -14357508;
    }



    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Circle representing the button
        if (on) {
            d.stroke(onOutlineColor);
            d.strokeWeight(3);
        } else {
            d.stroke(0);
            d.strokeWeight(1);
        }
        
        d.fill(250);
        d.ellipseMode(PConstants.CENTER);
        d.circle(x, y, radius * 2);
        
        // Text
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.textSize(radius);
        d.fill(0);
        d.text(text, x, y);
        
        d.pop();           // Restore original settings
    }



    public boolean contains(int x, int y) {
        double distanceX = x - this.x;
        double distanceY = y - this.y;
        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        if (distance <= radius) {
            return true;
        } else {
            return false;
        }
    }



    public int getFloorNum() {
        return this.floorNum;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
    
}
