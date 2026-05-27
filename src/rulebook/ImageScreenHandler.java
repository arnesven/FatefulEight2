package rulebook;

import view.MyColors;
import view.ScreenHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageScreenHandler extends ScreenHandler {
    public BufferedImage getImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(MyColors.BLACK.toAwtColor());
        g.fillRect(0, 0, width, height);
        drawBackground(g);
        drawForeground(g, 0, 0);
        return img;
    }
}
