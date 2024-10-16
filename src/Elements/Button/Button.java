package Elements.Button;

import java.awt.Point;
import java.util.ArrayList;

import processing.core.PApplet;
import Main.Main;

public class Button {

    private int x;
    private int y;
    private int width;
    private int height;
    private int cornerRounding;
    private int strokeWeight;
    private int stroke;
    private int fillColor;
    private int hoveredColor;
    private int textSize;
    private String text;

    private ArrayList<ButtonListener> listeners;

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
        this.stroke = 0;
        this.fillColor = 240;
        this.hoveredColor = 235;
        this.textSize = height / 2;
        this.text = null;

        listeners = new ArrayList<>();
    }

    public void draw(PApplet d) {
        d.push();          // Save original settings
        
        // Update button
        Point mouse = Main.getScaledMouse(d);
        boolean hovered = contains(mouse.x, mouse.y);

        // Rect representing the button
        d.fill(hovered ? hoveredColor : fillColor);
        d.rectMode(PApplet.CORNER);
        if (strokeWeight == 0) {
            d.noStroke();
        } else {
            d.strokeWeight(strokeWeight);
        }
        d.stroke(stroke);
        d.rect(x, y, width, height, cornerRounding);

        // Text
        if (text != null) {
            d.fill(0);
            d.textAlign(PApplet.CENTER, PApplet.CENTER);
            d.textSize(textSize);
            d.text(text, x + width/2, y + height/2);
        }
        
        d.pop();           // Restore original settings
    }

    public void mousePressed(int x, int y) {
        if (contains(x, y)) {
            notifyListeners();
        }
    }

    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setCornerRounding(int cornerRounding) {
		this.cornerRounding = cornerRounding;
	}

	public void setStrokeWeight(int strokeWeight) {
		this.strokeWeight = strokeWeight;
	}

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public void setStroke(int r, int g, int b) {
        this.stroke = Main.getInstance().color(r, g, b);
    }

	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}

    public void setFillColor(int r, int g, int b) {
        this.fillColor = Main.getInstance().color(r, g, b);
    }

	public void setHoveredColor(int hoveredColor) {
		this.hoveredColor = hoveredColor;
	}

    public void setHoveredColor(int r, int g, int b) {
        this.hoveredColor = Main.getInstance().color(r, g, b);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void addListener(ButtonListener listener) {
        listeners.add(listener);
    }

	private boolean contains(int x, int y) {
        if (x < this.x) return false;
        if (x > this.x + width) return false;
        if (y < this.y) return false;
        if (y > this.y + height) return false;
        
        return true;
    }

    private void notifyListeners() {
        for (ButtonListener listener : listeners) {
            listener.onClick(this);
        }
    }

}
