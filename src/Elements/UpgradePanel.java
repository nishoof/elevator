package Elements;

import processing.core.PApplet;
import processing.core.PConstants;

public class UpgradePanel {

    private final int x = 590;
    private final int y = 70;
    private final int width = 300;
    private final int height = 400;

    private boolean visible;

    public void draw(PApplet d) {
        if (!visible) return;

        d.push();          // Save original settings

        d.fill(255);
        d.stroke(0);
        d.strokeWeight(3);
        d.rect(x, y, width, height);

        d.fill(0);
        d.textSize(30);
        d.textAlign(PConstants.CENTER, PConstants.TOP);
        d.text("Upgrades", x + width/2, y + 20);

        d.pop();           // Restore original settings
    }

    public void toggleVisible() {
        visible = !visible;
    }

}
