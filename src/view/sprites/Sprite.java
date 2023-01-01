package view.sprites;

import view.MyColors;
import view.SpriteManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erini02 on 24/04/16.
 */
public class Sprite implements Serializable {

    private static int called = 0;
    private List<Sprite> layers;
    private String name;
    private String mapPath;
    private int row;
    private int column;
    private int width;
    private int height;
    private int resizeWidth;
    private int resizeHeight;
    private MyColors color1 = MyColors.BLACK;
    private MyColors color2 = MyColors.WHITE;
    private MyColors color3 = MyColors.CYAN;
    private MyColors color4 = MyColors.LIGHT_YELLOW;
    private double rotation = 0.0;
    private int frames;
    private boolean looping;
    private int downShift = 0;

    public Sprite(String name, String mapPath, int column, int row, int width, int height,
                  List<Sprite> layers){
        this.name = name;
        this.mapPath = mapPath;
        this.row = row;
        this.column = column;
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.layers = layers;
        this.resizeWidth = width;
        this.resizeHeight = height;
        frames = 1;
        this.looping = isAnyLooping(layers);
        SpriteManager.register(this);
        try {
            getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (++called > 50000) {
            throw new RuntimeException("Created more than 50000 sprites, really OK?");
        }
        System.out.println(called + " " + this.toString());

    }

    public static void resetCallCount() {
        called = 0;
    }

    private boolean isAnyLooping(List<Sprite> layers) {
        for (Sprite sp : layers) {
            if (sp.isLooping()) {
                return true;
            }
        }
        return false;
    }

    private boolean allLooping() {
        for (Sprite sp : layers) {
            if (!isLooping()) {
                return false;
            }
        }
        return true;
    }

    public Sprite(Sprite other, String suffix) {
        this(other.name + suffix, other.mapPath, other.getColumn(), other.getRow(), other.getWidth(), other.getHeight(), other.getLayers());
    }

    public static Sprite blankSprite() {
        return new Sprite("dummy", "animal.png", 0, null);
    }


    protected List<Sprite> getLayers() {
        return layers;
    }


    public Sprite(String name, String mapPath, int column, int row, int width, int height){
        this(name, mapPath, column, row, width, height, new ArrayList<>());
    }


    public Sprite(String name, String mapPath, int column, int row) {
        this(name, mapPath, column, row, 128, 128);
    }

    public Sprite(String name, String mapPath, int column, int row, int width, int height, int resizeW, int resizeH) {
        this(name, mapPath, column, row, width, height);
        this.resizeWidth = resizeW;
        this.resizeHeight = resizeH;
    }

    public Sprite(String name, String mapPath, int column) {
        this(name, mapPath, column, 0);
    }

    public Sprite(String name, List<Sprite> list) {
        this(name, list.get(0).getMap(), list.get(0).getColumn(),
                list.get(0).getRow(), list.get(0).getWidth(),
                list.get(0).getHeight(), new ArrayList<>(list.subList(1, list.size())));
    }

    public Sprite(String name, String mapPath, int column, List<Sprite> list) {
        this(name, mapPath, column, 0, 32, 32, list);
    }

    public String getName(){
        return name + (int)getRotation();
    }

    public BufferedImage getImage() throws IOException {
        if (SpriteCache.has(this)) {
            return SpriteCache.get(this);
        }
        BufferedImage img = internalGetImage();
        SpriteCache.put(this, img);
        return img;
     }

    public static String makePath(String[] strings) {
        StringBuffer buf = new StringBuffer();

        for (String part : strings) {
            buf.append(part + File.separator);
        }

        return buf.toString();
    }

    protected BufferedImage internalGetImage() throws IOException {
        int maxFrames = findMaxFrames();
        BufferedImage result = new BufferedImage(width*maxFrames, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = result.getGraphics();
        BufferedImage img = SpriteManager.getFile(makePath(new String[]{"resources", "sprites"}) + mapPath);
        img = img.getSubimage(column * width, row * height, width*frames, height);

        Graphics2D g2d = (Graphics2D) g;

        for (int n = 0; n < maxFrames; ++n) {
            g2d.drawImage(img, n*img.getWidth(), downShift, null);
        }
        for (Sprite s : layers) {
            for (int n = 0; n < maxFrames; ++n) {
                g2d.drawImage(s.internalGetImage(), n * (s.getWidth()*s.getFrames()), downShift, null);
            }
        }


        result = colorize(result, color1.toAwtColor(), color2.toAwtColor(), color3.toAwtColor(), color4.toAwtColor());


        if (this.resizeWidth != this.width || this.resizeHeight != this.height) {
            // image needs to be resized!
            result = resize(result);
        }

        return result;
    }

    private int findMaxFrames() {
        int max = frames;
        for (Sprite s : layers) {
            if (s.frames > max) {
                max = s.frames;
            }
        }
        return max;
    }

    private BufferedImage resize(BufferedImage result) {
        BufferedImage dimg = new BufferedImage(this.resizeWidth, this.resizeHeight, result.getType());
        Graphics2D gnew = dimg.createGraphics();
        gnew.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gnew.drawImage(result, 0, 0,
                this.resizeWidth, this.resizeHeight,
                0, 0,
                this.width, this.height, null);
        gnew.dispose();
        return dimg;
    }


    private static BufferedImage colorize(BufferedImage original, Color color1, Color color2, Color color3, Color color4) {
        for (int y = 0; y < original.getHeight(); ++y) {
            for (int x = 0; x < original.getWidth(); ++x) {
                if (original.getRGB(x, y) == 0xFF000000) {
                    original.setRGB(x, y, color1.getRGB());
                } else if (original.getRGB(x, y) == 0xFFFFFFFF) {
                    original.setRGB(x, y, color2.getRGB());
                } else if (original.getRGB(x, y) == 0xFFFF0000) {
                    original.setRGB(x, y, color3.getRGB());
                } else if (original.getRGB(x, y) == 0xFF0000FF) {
                    original.setRGB(x, y, color4.getRGB());
                }
            }
        }
        return original;
    }


    public String getMap() {
        return mapPath;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setColor1(MyColors color) {
        this.color1 = color;
        SpriteCache.invalidate(this);
    }

    public void setColor2(MyColors color) {
        this.color2 = color;
        SpriteCache.invalidate(this);
    }

    public void setColor3(MyColors color) {
        this.color3 = color;
        SpriteCache.invalidate(this);
    }

    public void setColor4(MyColors color) {
        this.color4 = color;
        SpriteCache.invalidate(this);
    }

    public void addToOver(Sprite piratemask) {
        layers.add(piratemask);
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double d) {
        rotation = d;
        SpriteManager.register(this);
        try {
            getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public void setLooping(boolean b) {
        this.looping = b;
    }

    public boolean isLooping() {
        return looping;
    }

    public void registerYourself() {
        SpriteManager.register(this);
        try {
            getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered() {
        return SpriteManager.isRegistered(this);
    }

    public void setColumn(int i) {
        this.column = i;
    }


    public int getMaximumFrames() {
        return findMaxFrames();
    }

    public void shiftUpPx(int i) {
        downShift = -i;
    }

    public void setName(String s) {
        this.name = s;
    }


}
