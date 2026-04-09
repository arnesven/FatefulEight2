package model.states.mine;

import model.items.treasures.DiamondTreasureItem;
import model.items.treasures.TreasureItem;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class DiamondGeodeObject extends GeodeRockObject {

    private static MyColors LINE_COLOR = MyColors.CYAN;
    private static final MyColors FILL_COLOR = MyColors.WHITE;

    private static final Sprite[] SPRITES = GeodeRockObject.makeGeodeSpriteSet(LINE_COLOR, FILL_COLOR);

    private static final Sprite32x32 FLOATING_SPRITE =
            new Sprite32x32("floatingdiamond", "warehouse.png", 0x45,
                    LINE_COLOR, FILL_COLOR, MyColors.GRAY_RED);
    private final Sprite sprite;

    public DiamondGeodeObject() {
        super("Diamond", 18, FLOATING_SPRITE);
        this.sprite = SPRITES[MyRandom.randInt(4)];
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    protected TreasureItem makeGemItem() {
        return new DiamondTreasureItem();
    }

    @Override
    public MineObject copy() {
        return new DiamondGeodeObject();
    }
}
