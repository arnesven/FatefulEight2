package view.widget;

import model.characters.GameCharacter;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthBar {
    private static final double MAX_PARTS = 7*4;
    private static final Map<MyColors, Sprite> FULL_BAR = makeBarSpriteMap("fullbar", 0xB8);
    private static final List<Map<MyColors, Sprite>> PARTIAL_BARS = List.of(
            makeBarSpriteMap("bar1", 0xBB),
            makeBarSpriteMap("bar2", 0xBA),
            makeBarSpriteMap("bar3", 0xB9));

    private static Map<MyColors, Sprite> makeBarSpriteMap(String name, int num) {
        MyColors colors[] = new MyColors[]{MyColors.RED, MyColors.ORANGE, MyColors.YELLOW, MyColors.GREEN};
        Map<MyColors, Sprite> map = new HashMap<>();
        for (MyColors color : colors) {
            Sprite sprite = new Sprite8x8(name+color.name(), "charset.png", num,
                    color, MyColors.WHITE, color, color);
            map.put(color, sprite);
        }
        return map;
    }

    public static void drawHealthBar(ScreenHandler screenHandler, GameCharacter gc, int col, int row) {
        double percentage = (double)gc.getHP() / (double) gc.getMaxHP();
        int parts = (int)Math.ceil(percentage * MAX_PARTS);
        MyColors color = getBarColor(percentage, gc.getHP());
        for (int i = 0; i < parts/4; ++i) {
            screenHandler.put(col + i, row, FULL_BAR.get(color));
        }
        if (parts % 4 != 0) {
            screenHandler.put(col + parts/4, row, PARTIAL_BARS.get((parts % 4) - 1).get(color));
        }
    }

    private static MyColors getBarColor(double percentage, int hp) {
        if (hp <= 2) {
            return MyColors.RED;
        }
        if (percentage >= 0.85) {
            return MyColors.GREEN;
        } else if (percentage >= 0.5) {
            return MyColors.YELLOW;
        } else if (percentage >= 0.25) {
            return MyColors.ORANGE;
        }
        return MyColors.RED;
    }

    public static MyColors getHealthColor(int hp, int maxHP) {
        double percentage = (double)hp / (double) maxHP;
        return getBarColor(percentage, hp);
    }
}
