package Main;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;

public class DataHolder {

    private static PFont regularFont;
    private static PFont mediumFont;

    private static PImage heartImg;

    public static void init(PApplet d) {
        heartImg = d.loadImage("images/heart.png");
        heartImg.resize(50, 50);

        regularFont = d.createFont("fonts/GeistMono-Regular.otf", 128);
        mediumFont = d.createFont("fonts/GeistMono-Medium.otf", 128);
        d.textMode(PConstants.MODEL);
    }

    public static PFont getRegularFont() {
        return regularFont;
    }

    public static void setRegularFont(PFont regular) {
        DataHolder.regularFont = regular;
    }

    public static PFont getMediumFont() {
        return mediumFont;
    }

    public static void setMediumFont(PFont medium) {
        DataHolder.mediumFont = medium;
    }

    public static PImage getHeartImg() {
        return heartImg;
    }

    public static void setHeartImg(PImage heartImg) {
        DataHolder.heartImg = heartImg;
    }

}
