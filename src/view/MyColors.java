package view;

import view.sprites.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public enum MyColors {
    BLACK       (new Color(0x000000)),
    WHITE       (new Color(0xFFFFFF)),
    DARK_RED    (new Color(0x880000)),
    CYAN        (new Color(0xAAFFEE)),
    DARK_PURPLE (new Color(0x772277)),
    PURPLE      (new Color(0xCC44CC)),
    GREEN       (new Color(0x00CC55)),
    DARK_BLUE   (new Color(0x000066)),
    BLUE        (new Color(0x0000AA)),
    LIGHT_YELLOW (new Color(0xEEEE77)),
    PEACH       (new Color(0xDD8855)),
    DARK_BROWN  (new Color(0x351000)),
    BROWN       (new Color(0x664400)),
    GRAY_RED    (new Color(0x775544)),
    LIGHT_RED   (new Color(0xFF7777)),
    PINK        (new Color(0xFFBBAA)),
    LIGHT_PINK  (new Color(0xFFDDCC)),
    DARK_GRAY   (new Color(0x333333)),
    GRAY        (new Color(0x777777)),
    LIGHT_GREEN (new Color(0xAAFF66)),
    LIGHT_BLUE  (new Color(0x0088FF)),
    LIGHT_GRAY  (new Color(0xBBBBBB)),
    DARK_GREEN  (new Color(0x026502)),
    TAN         (new Color(0x666633)),
    GOLD        (new Color(0xAA7700)),
    YELLOW      (new Color(0xFFDD00)),
    RED         (new Color(0xDD0000)),
    BEIGE       (new Color(0xFFEEAA)),
    ORANGE      (new Color(0xFF9900)),
    ORC_GREEN   (new Color(0x77DD88));

    private Color awtColor;
    
    MyColors(Color color) {
	    this.awtColor = color;
    }

    public Color toAwtColor() {
	    return this.awtColor;
    }

    private static Map<Integer, Integer> closestMap = new HashMap<>();

    public static void transformImage(Sprite imgsprite) {
        try {
            BufferedImage img = imgsprite.getImage();
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int rgb = img.getRGB(x, y);
                    if (closestMap.containsKey(rgb)) {
                        img.setRGB(x, y, closestMap.get(rgb));
                    } else {
                        int closest = findClosest(rgb);
                        closestMap.put(rgb, closest);
                        img.setRGB(x, y, closest);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int findClosest(int rgb) {
        long minDist = Long.MAX_VALUE;
        MyColors minColor = null;
        for (MyColors c : MyColors.values()) {
            long dist = distance(c.toAwtColor().getRGB(), rgb);
            if (dist < minDist) {
                minDist = dist;
                minColor = c;
            }
        }
        return minColor.toAwtColor().getRGB();
    }

    private static long distance(int rgb1, int rgb2) {
        long diffR = getRed(rgb1) - getRed(rgb2);
        long diffG = getGreen(rgb1) - getGreen(rgb2);
        long diffB = getBlue(rgb1) - getBlue(rgb2);
        return diffR*diffR + diffG*diffG + diffB*diffB;
    }

    private static int getBlue(int rgb) {
        return ((rgb-0xFF000000) % 0x100);
    }

    private static int getGreen(int rgb) {
        return ((rgb - 0xFF000000) % 0x10000) / 0x100;
    }

    private static int getRed(int rgb) {
        return (rgb - 0xFF000000) / 0x10000;
    }
}
