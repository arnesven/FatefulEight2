package view.sprites;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteCache {

    private static Map<Sprite, BufferedImage> map = new HashMap<>();
    private static boolean warned = false;
    private static Sprite lastAdded;

    public static boolean has(Sprite sprite) {
        return map.containsKey(sprite);
    }

    public static BufferedImage get(Sprite sprite) {
        return map.get(sprite);
    }

    public static void put(Sprite sprite, BufferedImage img) {
        map.put(sprite, img);
        lastAdded = sprite;
    }

    public static void invalidate(Sprite sprite) {
        map.remove(sprite);
    }

    public static void checkForClear() {
        if (map.size() >= 100 && map.size() % 100 == 0) {
            if (!warned) {
                System.out.println("Sprite cache size = " + map.size() + ", last added was " + lastAdded.getName());
                warned = true;
            }
        } else {
            warned = false;
        }
    }
}
