package view;

import view.sprites.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by erini02 on 24/04/16.
 */
public class SpriteManager {
    private static Map<String, Sprite> nameMap = new HashMap<>();
    private static Map<String, BufferedImage> filemap = new HashMap<>();

    public static void register(Sprite sprite) {
        nameMap.put(sprite.getName(), sprite);
    }

    public static BufferedImage getFile(String s) throws IOException {
        BufferedImage img = filemap.get(s);
        if (img == null) {
            System.out.println("Smurfing for '" + s + "' Img is null, loading image.");
            try {
                File f = new File(s);
                InputStream is;
                if (f.exists()) {
                    is = new FileInputStream(s);
                } else {
                    String path = "/" + s.replace("resources/", "");
                    is = SpriteManager.class.getResourceAsStream(path);
                }
                img = ImageIO.read(is);
                filemap.put(s, img);
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
                throw iae;
            }
        }
        return img;
    }

    public static boolean isRegistered(Sprite sprite) {
        return nameMap.containsValue(sprite);
    }

    public static boolean isRegistered(String key) {
        return nameMap.containsKey(key);
    }

    public static Sprite getSprite(String key) {
        return nameMap.get(key);
    }
}
