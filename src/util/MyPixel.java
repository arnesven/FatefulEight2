package util;

public class MyPixel {
    private int alpha;
    private final int red;
    private final int green;
    private final int blue;

    public MyPixel(int rgb) {
        this.alpha = (rgb >> 24) & 0x000000FF;
        this.red   = (rgb & 0x00FF0000) >> 16;
        this.green = (rgb & 0x0000FF00) >>  8;
        this.blue  = rgb & 0x000000FF;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int toInt() {
        return (alpha << 24) + (red << 16) + (green << 8) + blue;
    }

    public void setAlpha(int i) {
        this.alpha = i;
    }
}
