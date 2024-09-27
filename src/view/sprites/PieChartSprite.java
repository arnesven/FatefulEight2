package view.sprites;

import view.MyColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PieChartSprite extends Sprite {
    private static final int MARGIN_PX = 2;
    private static int count = 0;
    private final List<Double> fractions;
    private final List<MyColors> colors;

    public PieChartSprite(int width, int height, List<Double> slices, List<MyColors> colors) {
        super("piechartNR" + getCounter(), "splash.png", 0, 0,
                width, height, new ArrayList<>());
        this.colors = colors;
        SpriteCache.invalidate(this);
        this.fractions = slices;
        try {
            getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getCounter() {
        return count++;
    }

    @Override
    protected BufferedImage internalGetImage() throws IOException {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.translate(MARGIN_PX, MARGIN_PX);
        if (fractions != null) {
            double startAngle = 90;
            int index = 0;
            for (double slice : fractions) {
                double current = slice * 360.0;
                g.setColor(colors.get(index++).toAwtColor());
                g.fillArc(MARGIN_PX, MARGIN_PX, getWidth() - 2*MARGIN_PX, getHeight() - 2*MARGIN_PX,
                        (int)startAngle, (int)Math.floor(-current));
                startAngle -= current;
            }
        }
        return img;
    }
}
