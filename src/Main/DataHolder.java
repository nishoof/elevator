package Main;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;

/**
 * Utility class for managing fonts and images in the data folder.
 */
public final class DataHolder {

    private static PFont regularFont;
    private static PFont mediumFont;

    private static PImage heartImg;

    // Private constructor to prevent instantiation
    private DataHolder() {
    }

    public static void init(PApplet d) {
        regularFont = d.createFont("fonts/GeistMono-Regular.otf", 128);
        mediumFont = d.createFont("fonts/GeistMono-Medium.otf", 128);
        d.textMode(PConstants.MODEL);

        heartImg = d.loadImage("images/heart.png");
        heartImg.resize(50, 50);
    }

    public static PFont getRegularFont() {
        return regularFont;
    }

    public static PFont getMediumFont() {
        return mediumFont;
    }

    public static PImage getHeartImg() {
        return heartImg;
    }

}
