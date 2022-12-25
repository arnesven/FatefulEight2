package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawingArea extends JComponent {
    private static final int SCREEN_HEIGHT = 400;
    private static final int SCREEN_WIDTH = 640;
    public static final int WINDOW_COLUMNS = SCREEN_WIDTH / 8;
    public static final int WINDOW_ROWS = SCREEN_HEIGHT / 8;
    private ScreenHandler screenHandler;
    private static int magnification;
    private BufferedImage buffer = null;

    public DrawingArea() {
        this.magnification = 2;
        screenHandler = new ScreenHandler();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(MyColors.DARK_GRAY.toAwtColor());
        g.fillRect(0, 0, getWidth(), getHeight());
        if (this.buffer != null) {
            BufferedImage b2 = new BufferedImage(buffer.getWidth(), buffer.getHeight(), buffer.getType());
            Graphics g2 = b2.getGraphics();
            g2.drawImage(buffer, 0, 0, null);
            if (screenHandler.foregroundEnabled()) {
                screenHandler.drawForeground(g2, 0, 0);
            }
            int xoffset = (getWidth() - SCREEN_WIDTH*magnification) / 2;
            int yoffset = (getHeight() - SCREEN_HEIGHT*magnification) / 2;
            g.setColor(MyColors.BLACK.toAwtColor());
            g.fillRect(xoffset, yoffset,
                    SCREEN_WIDTH * magnification,
                    SCREEN_HEIGHT * magnification);
            g.drawImage(b2, xoffset, yoffset, b2.getWidth()*magnification, b2.getHeight()*magnification, null);
        }
    }

    public static int getMagnification() {
        return magnification;
    }

    public void update() {
        this.buffer = null;
        BufferedImage img = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        screenHandler.drawBackground(g);
        this.buffer = img;
    }

    public ScreenHandler getScreenHandler() {
        return screenHandler;
    }
}

