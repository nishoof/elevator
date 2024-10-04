import processing.core.PApplet;
import processing.core.PConstants;

public class ElevatorButton {
    
    private int x;
    private int y;
    private int floorNum;
    private String text;
    private int size;
    private int cornerRounding;
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
        this.size = 32;
        this.on = false;
        this.onOutlineColor = -14357508;
    }

    /**
     * Constructs a new ElevatorButton
     * @param x x-coordinate of the center of this ElevatorButton
     * @param y y-coordinate of the center of this ElevatorButton
     * @param floorNum floor number of this ElevatorButton
     * @param size the size of the UI element (half the width / height of the square)
     */
    public ElevatorButton(int x, int y, int floorNum, int size, int cornerRounding) {
        this.x = x;
        this.y = y;
        this.floorNum = floorNum;
        this.text = String.valueOf(floorNum);
        this.size = size;
        this.cornerRounding = cornerRounding;
        this.on = false;
        this.onOutlineColor = -14357508;
    }

    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Decide the stroke color and weight based on whether the button is on
        if (on) {
            d.stroke(onOutlineColor);
            d.strokeWeight(4);
        } else {
            d.stroke(0);
            d.strokeWeight(3);
        }

        // Square representing the button
        d.fill(250);
        d.rectMode(PConstants.CENTER);
        d.rect(x, y, size * 2, size * 2, cornerRounding);
        
        // Text
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.textSize(size * 7 / 6);
        d.fill(0);
        d.text(text, x, y);
        
        d.pop();           // Restore original settings
    }

    public boolean contains(int x, int y) {
        if (x < this.x - size) return false;
        if (x > this.x + size) return false;
        if (y < this.y - size) return false;
        if (y > this.y + size) return false;
        
        return true;
    }

    public int getFloorNum() {
        return this.floorNum;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
    
}
