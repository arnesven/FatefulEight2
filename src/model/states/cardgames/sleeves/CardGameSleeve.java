package model.states.cardgames.sleeves;

import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;

import java.util.List;

import static view.sprites.HandAnimationSprite.*;

public abstract class CardGameSleeve {

    private static final List<MyColors> BASE_COLORS = List.of(MyColors.DARK_GREEN, MyColors.DARK_PURPLE, MyColors.GOLD,
            MyColors.DARK_BROWN, MyColors.DARK_BLUE, MyColors.DARK_RED);
    private static final List<MyColors> DETAIL_COLORS = List.of(MyColors.GREEN, MyColors.PURPLE, MyColors.YELLOW,
            MyColors.ORANGE, MyColors.BLUE, MyColors.RED);

    private final Sprite sprite;

    public CardGameSleeve(MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        this.sprite = new Sprite("handanioverlay", "cardgame.png", 0, 3,
                SLICE_WIDTH*MAX_FRAMES, SLICE_HEIGHT);
        sprite.setColor1(color1);
        sprite.setColor2(color2);
        sprite.setColor3(color3);
        sprite.setColor4(color4);
    }

    public Sprite getSprite() {
        return sprite;
    }


    public static CardGameSleeve makeRandomSleeve() {
        int dieRoll = MyRandom.rollD6();
        return switch (dieRoll) {
            case 1 -> new ShortSleeve(MyRandom.sample(BASE_COLORS));
            case 2 -> new PlainLongSleeve(MyRandom.sample(BASE_COLORS));
            case 3 -> new FancyLongSleeve(MyRandom.sample(BASE_COLORS), MyRandom.sample(DETAIL_COLORS));
            case 4 -> new BraceletSleeve(MyRandom.sample(DETAIL_COLORS));
            case 5 -> new ShortSleeveWithBracelet(MyRandom.sample(BASE_COLORS), MyRandom.sample(DETAIL_COLORS));
            default -> new NoSleeveAtAll();
        };
    }

}
