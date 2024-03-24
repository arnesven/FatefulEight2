package view.sprites;

import view.MyColors;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CharSprite extends Sprite8x8 {

    private static Map<String, CharSprite> cache = new HashMap<>();
    public static final Sprite FEMALE = make(0x83, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK);
    public static final Sprite MALE = make(0x82, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.CYAN);

    public static CharSprite make(char letter, MyColors color1) {
        return make(letter, color1, MyColors.CYAN, MyColors.BLACK);
    }

    public static CharSprite make(char letter,  MyColors color1, MyColors color2, MyColors color3) {
        return make((int)letter, color1, color2, color3);
    }

    public static CharSprite make(int num, MyColors color1, MyColors color2, MyColors color3) {
        String key = num + color1.name().toString() + color2.name().toString() + color3.name().toString();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        CharSprite toReturn = new CharSprite(num, color1, color2, color3);
        cache.put(key, toReturn);
        return toReturn;
    }

    private CharSprite(int num, MyColors color1, MyColors color2, MyColors color3) {
        super("charset"+num, "charset.png", num);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
    }
}
