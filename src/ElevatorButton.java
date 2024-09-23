import processing.core.PApplet;
import processing.core.PConstants;

public class ElevatorButton implements DrawableObject {
    
    private int x;
    private int y;
    private int floorNum;
    private String text;
    private int radius;
    private boolean on;
    private int onOutlineColor;



    public ElevatorButton(int x, int y, int floorNum) {
        this.x = x;
        this.y = y;
        this.floorNum = floorNum;
        this.text = String.valueOf(floorNum);
        this.radius = 32;
        this.on = false;
        this.onOutlineColor = -14357508;
    }



    @Override
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
        d.textSize(32);
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
