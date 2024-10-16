package Elements.Button;

import processing.core.PApplet;
import processing.core.PConstants;

public class ElevatorButton extends Button {
    
    private final int ON_OUTLINE_COLOR;
    
    private int floorNum;
    private String text;
    private int size;

    /**
     * Constructs a new ElevatorButton
     * @param x x-coordinate of the center of this ElevatorButton
     * @param y y-coordinate of the center of this ElevatorButton
     * @param floorNum floor number of this ElevatorButton
     * @param size the size of the UI element (half the width / height of the square)
     */
    public ElevatorButton(int x, int y, int floorNum, int size, int cornerRounding) {
        super(x, y, size * 2, size * 2);
        this.floorNum = floorNum;
        this.text = String.valueOf(floorNum);
        this.size = size;
        this.ON_OUTLINE_COLOR = -14357508;
        setCornerRounding(cornerRounding);
    }

    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Draw the button
        super.draw(d);
        
        // Text
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.textSize(size * 7 / 6);
        d.fill(0);
        d.text(text, getX(), getY(), getWidth(), getHeight());
        
        d.pop();           // Restore original settings
    }

    public int getFloorNum() {
        return this.floorNum;
    }

    public void setOn(boolean on) {
        setStroke(on ? ON_OUTLINE_COLOR : 0);
        setStrokeWeight(on ? 4 : 3);
    }

}
