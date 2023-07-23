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
            System.out.print("Smurfing for '" + s + "' Img is null, loading image.");
            try {
                File f = new File(s);
                InputStream is;
                if (f.exists()) {
                    System.out.println(" - existed.");
                    is = new FileInputStream(s);
                } else {
                    System.out.println(" - didn't exist. Trying modified path.");
                    String path = s;
                    if (s.contains("resources/")) { // linux
                        path = s.replace("resources/", "");
                    } else if (s.contains("resources\\")) { // windows
                        path = s.replace("resources\\", "");
                    }
                    is = SpriteManager.class.getClassLoader().getResourceAsStream(path);
                    if (is == null) {
                        System.out.println("Trying with replaced slashes");
                        is = SpriteManager.class.getClassLoader().getResourceAsStream(path.replaceAll("\\\\", "/"));
                    }
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
