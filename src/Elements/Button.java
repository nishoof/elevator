package Elements;

import java.awt.Point;
import processing.core.PApplet;
import Main.Main;

public class Button {
    
    private int x;
    private int y;
    private int width;
    private int height;
    private int cornerRounding;
    private int strokeWeight;
    private int fillColor;
    private int hoveredColor;

    /**
     * Constructs a new Button
     * @param x x-coordinate of the top-left corner of this Button
     * @param y y-coordinate of the top-left corner of this Button
     * @param width width of this Button
     * @param height height of this Button
     */
    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRounding = 8;
        this.strokeWeight = 3;
        this.fillColor = 240;
        this.hoveredColor = 235;
    }

    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Update button
        Point mouse = Main.getScaledMouse(d);
        boolean hovered = contains(mouse.x, mouse.y);

        // Square representing the button
        d.fill(hovered ? hoveredColor : fillColor);
        d.rectMode(PApplet.CORNER);
        d.strokeWeight(strokeWeight);
        d.rect(x, y, width, height, cornerRounding);
        
        d.pop();           // Restore original settings
    }

    public boolean contains(int x, int y) {
        if (x < this.x) return false;
        if (x > this.x + width) return false;
        if (y < this.y) return false;
        if (y > this.y + height) return false;
        
        return true;
    }

}
