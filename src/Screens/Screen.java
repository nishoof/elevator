package Screens;

import processing.core.PApplet;

public interface Screen {
    public void draw(PApplet d);

    public void mousePressed(int mouseX, int mouseY);

    public void keyPressed(char key);
}
