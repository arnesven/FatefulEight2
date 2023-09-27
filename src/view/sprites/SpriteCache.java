package view.sprites;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteCache {

    private static Map<Sprite, BufferedImage> map = new HashMap<>();

    public static boolean has(Sprite sprite) {
        return map.containsKey(sprite);
    }

    public static BufferedImage get(Sprite sprite) {
        return map.get(sprite);
    }

    public static void put(Sprite sprite, BufferedImage img) {
        if (map.size() > 10000) {
            System.out.println("Clearing sprite cache!");
            map.clear();
        }
        map.put(sprite, img);
    }

    public static void invalidate(Sprite sprite) {
        map.remove(sprite);
    }
}
