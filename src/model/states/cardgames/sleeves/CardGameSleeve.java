package model.states.cardgames.sleeves;

import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x32;
import view.sprites.Sprite32x16;
import view.sprites.Sprite32x32;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static view.sprites.HandAnimationSprite.*;

public abstract class CardGameSleeve {

    private static final List<MyColors> BASE_COLORS = List.of(MyColors.DARK_GREEN, MyColors.DARK_PURPLE, MyColors.GOLD,
            MyColors.DARK_BROWN, MyColors.DARK_BLUE, MyColors.DARK_RED);
    private static final List<MyColors> DETAIL_COLORS = List.of(MyColors.GREEN, MyColors.PURPLE, MyColors.YELLOW,
            MyColors.ORANGE, MyColors.BLUE, MyColors.RED);

    private final Sprite sprite;
    private Map<Integer, Sprite32x32> handSprites;

    public CardGameSleeve(MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        this.sprite = new Sprite("handanioverlay", "cardgame.png", 0, 4,
                SLICE_WIDTH*MAX_FRAMES, SLICE_HEIGHT);
        sprite.setColor1(color1);
        sprite.setColor2(color2);
        sprite.setColor3(color3);
        sprite.setColor4(color4);
        handSprites = new HashMap<>();
        handSprites.put(0, new Sprite32x32("sleevehand1", "cardgame.png", 0x30,
                color1, color2, color3, color4));
        handSprites.put(90, new Sprite32x32("sleevehand2", "cardgame.png", 0x30,
                color1, color2, color3, color4));
        handSprites.get(90).setRotation(90);
        handSprites.put(180, new Sprite32x32("sleevehand3", "cardgame.png", 0x30,
                color1, color2, color3, color4));
        handSprites.get(180).setRotation(180);
        handSprites.put(270, new Sprite32x32("sleevehand4", "cardgame.png", 0x30,
                color1, color2, color3, color4));
        handSprites.get(270).setRotation(270);
    }

    public Sprite getSprite() {
        return sprite;
    }


    public static CardGameSleeve makeRandomSleeve() {
        if (MyRandom.flipCoin()) {
            return new BraceletSleeve(MyRandom.sample(DETAIL_COLORS));
        }
        return new FancyLongSleeve(MyRandom.sample(BASE_COLORS), MyRandom.sample(DETAIL_COLORS));
        /*
        int dieRoll = MyRandom.rollD6();
        return switch (dieRoll) {
            case 1 -> new ShortSleeve(MyRandom.sample(BASE_COLORS));
            case 2 -> new PlainLongSleeve(MyRandom.sample(BASE_COLORS));
            case 3 -> new FancyLongSleeve(MyRandom.sample(BASE_COLORS), MyRandom.sample(DETAIL_COLORS));
            case 4 -> new BraceletSleeve(MyRandom.sample(DETAIL_COLORS));
            case 5 -> new ShortSleeveWithBracelet(MyRandom.sample(BASE_COLORS), MyRandom.sample(DETAIL_COLORS));
            default -> new NoSleeveAtAll();
        };*/
    }

    public Sprite getHandSprite(int rotation) {
        return handSprites.get(rotation);
    }
}
