package Main;

import processing.core.PFont;

public class FontHolder {

    private static PFont regular;
    private static PFont medium;

    public static PFont getRegular() {
        return regular;
    }

    public static void setRegular(PFont regular) {
        FontHolder.regular = regular;
    }

    public static PFont getMedium() {
        return medium;
    }

    public static void setMedium(PFont medium) {
        FontHolder.medium = medium;
    }

}
