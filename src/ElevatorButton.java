import processing.core.PApplet;
import processing.core.PConstants;

public class ElevatorButton implements DrawableObject, ClickableObject {
    private int x;
    private int y;
    private String text;
    private int radius;
    private int color;



    public ElevatorButton(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.radius = 32;
        this.color = 100;
    }

    public ElevatorButton(int x, int y, String text, int radius, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }



    @Override
    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Circle representing the button
        d.ellipseMode(PConstants.CENTER);
        d.circle(x, y, radius * 2);
        
        // Text
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.textSize(32);
        d.fill(0);
        d.text(text, x, y);
        
        d.pop();           // Restore original settings
    }

    @Override
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

    @Override
    public void clicked() {
        System.out.println("BUTTON CLICKED");
    }
    
}
