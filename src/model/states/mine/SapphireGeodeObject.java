package model.states.mine;

import model.items.treasures.SapphireTreasureItem;
import model.items.treasures.TreasureItem;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class SapphireGeodeObject extends GeodeRockObject {

    private static final MyColors LINE_COLOR = MyColors.DARK_BLUE;
    private static final MyColors FILL_COLOR = MyColors.BLUE;

    private static final Sprite[] SPRITES = GeodeRockObject.makeGeodeSpriteSet(LINE_COLOR, FILL_COLOR);

    private static final Sprite32x32 FLOATING_SPRITE =
            new Sprite32x32("floatinsapphire", "warehouse.png", 0x44,
                    LINE_COLOR, FILL_COLOR, MyColors.GRAY_RED);
    private final Sprite sprite;

    public SapphireGeodeObject() {
        super("Sapphire", 11, FLOATING_SPRITE);
        this.sprite = SPRITES[MyRandom.randInt(4)];
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    protected TreasureItem makeGemItem() {
        return new SapphireTreasureItem();
    }
}
