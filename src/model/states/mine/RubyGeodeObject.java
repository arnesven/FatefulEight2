package model.states.mine;

import model.items.treasures.RubyTreasureItem;
import model.items.treasures.TreasureItem;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class RubyGeodeObject extends GeodeRockObject {

    private static final MyColors FILL_COLOR = MyColors.DARK_RED;
    private static final MyColors LINE_COLOR = MyColors.RED;

    private static final Sprite[] SPRITES = GeodeRockObject.makeGeodeSpriteSet(LINE_COLOR, FILL_COLOR);

    private static final Sprite32x32 FLOATING_SPRITE =
            new Sprite32x32("floatingruby", "warehouse.png", 0x46,
            LINE_COLOR, FILL_COLOR, MyColors.GRAY_RED);

    private final Sprite sprite;

    public RubyGeodeObject() {
        super("Ruby", 15, FLOATING_SPRITE);
        this.sprite = SPRITES[MyRandom.randInt(4)];
   }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    protected TreasureItem makeGemItem() {
        return new RubyTreasureItem();
    }

    @Override
    public MineObject copy() {
        return new RubyGeodeObject();
    }
}
