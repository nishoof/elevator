package Elements;

import Main.DataHolder;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Hint {

    private final int GREATEST_WIDTH = 350;
    private final int MAX_CHARS = 108;
    
    private int textSize;
    private PFont font;
    private String text;

    private int x;
    private int y;
    private int endingY;
    private int width;
    private int height;         // height of box. text won't be drawn within 3 px of box


    public Hint(int x, int y, String text) {
        // Position
        this.x = x;
        this.y = y;

        // Text
        this.font = DataHolder.getRegularFont();
        if (text.length() <= MAX_CHARS) {
            this.text = text;
        } else {
            this.text = text.substring(0, MAX_CHARS - 3) + "...";
        }
        this.textSize = 16;

        // Width
        int padding = 20;
        double charWidth = font.width('a');
        double textWidth = charWidth * text.length() * textSize;
        if (textWidth + padding < GREATEST_WIDTH) {
            this.width = (int) textWidth + padding;
        } else {
            this.width = GREATEST_WIDTH;
        }

        // Height
        this.height = 50;
    }

    public Hint(int x, int startingY, int endingY, String text) {
        this(x, startingY, text);
        this.endingY = endingY;
    }

    public void draw(PApplet d) {
        d.push();          // Save original settings

        updateY();

        // Box
        d.fill(230);
        d.stroke(0);
        d.strokeWeight(2);
        d.rectMode(PConstants.CENTER);
        d.rect(x, y, width, height, 10);

        // Text
        d.fill(0);
        d.textFont(font);
        d.textSize(textSize);
        d.textAlign(PConstants.CENTER, PConstants.CENTER);
        d.rectMode(PConstants.CENTER);
        d.text(text, x, y, width, height);

        d.pop();           // Restore original settings
    }

    public boolean contains(int x, int y) {
        if (x < this.x - width/2) return false;
        if (x > this.x + width/2) return false;
        if (y < this.y - height/2) return false;
        if (y > this.y + height/2) return false;
        
        return true;
    }

    private void updateY() {
        if (y != endingY) {
            if (y < endingY) {
                y += 5;
                if (y > endingY) {
                    y = endingY;
                }
            } else {
                y -= 5;
                if (y < endingY) {
                    y = endingY;
                }
            }
        }
    }

}
