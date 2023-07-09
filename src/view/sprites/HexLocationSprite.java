package view.sprites;

import view.MyColors;

import java.util.HashMap;
import java.util.Map;

public class HexLocationSprite extends Sprite32x16 {

    private static Map<String, HexLocationSprite> cache = new HashMap<>();

    private HexLocationSprite(String name, int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super(name, "world_foreground.png", num);
        this.setColor1(color1);
        this.setColor2(color2);
        this.setColor3(color3);
        this.setColor4(color4);
    }

    public static HexLocationSprite make(String name, int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        String key = name + num + color1.name() + color2.name() + color3.name() + color4.name();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        HexLocationSprite toReturn = new HexLocationSprite(name, num, color1, color2, color3, color4);
        cache.put(key, toReturn);
        return toReturn;
    }

    public static HexLocationSprite make(String name, int num, MyColors color1, MyColors color2, MyColors color3) {
        return make(name, num, color1, color2, color3, color3);
    }
}
