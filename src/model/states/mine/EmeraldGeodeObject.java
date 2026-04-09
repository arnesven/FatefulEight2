package model.states.mine;

import model.items.treasures.EmeraldTreasureItem;
import model.items.treasures.TreasureItem;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class EmeraldGeodeObject extends GeodeRockObject {

    private static final MyColors FILL_COLOR = MyColors.GREEN;
    private static final MyColors LINE_COLOR = MyColors.DARK_GREEN;

    private static final Sprite[] SPRITES = GeodeRockObject.makeGeodeSpriteSet(LINE_COLOR, FILL_COLOR);
    private final Sprite sprite;

    private static final Sprite32x32 FLOATING_SPRITE =
            new Sprite32x32("floatingemerald", "warehouse.png", 0x44,
                    LINE_COLOR, FILL_COLOR, MyColors.GRAY_RED);

    public EmeraldGeodeObject() {
        super("Emerald", 13, FLOATING_SPRITE);
        this.sprite = SPRITES[MyRandom.randInt(4)];
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    protected TreasureItem makeGemItem() {
        return new EmeraldTreasureItem();
    }

    @Override
    public MineObject copy() {
        return new EmeraldGeodeObject();
    }
}
