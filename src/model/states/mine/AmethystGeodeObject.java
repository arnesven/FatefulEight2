package model.states.mine;

import model.items.treasures.AmethystTreasureItem;
import model.items.treasures.TreasureItem;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class AmethystGeodeObject extends GeodeRockObject {
    private static final MyColors LINE_COLOR = MyColors.PURPLE;
    private static final MyColors FILL_COLOR = MyColors.DARK_PURPLE;

    private static final Sprite[] SPRITES = GeodeRockObject.makeGeodeSpriteSet(LINE_COLOR, FILL_COLOR);

    private static final Sprite32x32 FLOATING_SPRITE =
            new Sprite32x32("floatinsapphire", "warehouse.png", 0x46,
                    LINE_COLOR, FILL_COLOR, MyColors.GRAY_RED);
    private final Sprite sprite;

    public AmethystGeodeObject() {
        super("Amethyst", 9, FLOATING_SPRITE);
        this.sprite = SPRITES[MyRandom.randInt(4)];
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    protected TreasureItem makeGemItem() {
        return new AmethystTreasureItem();
    }
}
