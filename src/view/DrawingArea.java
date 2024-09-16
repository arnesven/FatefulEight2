package view;

import view.sprites.SpriteCache;

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
        magnification = 2;
        screenHandler = new ScreenHandler();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(MyColors.BLACK.toAwtColor());
        g.fillRect(0, 0, getWidth(), getHeight());
        if (this.buffer != null) {
            g.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
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
        if (screenHandler.foregroundEnabled()) {
            screenHandler.drawForeground(g, 0, 0);
        }
        if (screenHandler.getFadeLevel() > 0.0) {
            Color col = screenHandler.getFadeColor().toAwtColor();
            double d = Math.round(screenHandler.getFadeLevel() * 20.0) / 20.0;
            Color c2 = new Color(col.getRed(), col.getGreen(), col.getBlue(), (int)(0xFF * d));
            g.setColor(c2);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        this.buffer = img;
        SpriteCache.checkForClear();
    }

    public ScreenHandler getScreenHandler() {
        return screenHandler;
    }

    public void clear() {
        this.buffer = null;
        this.screenHandler = new ScreenHandler();
    }
}

