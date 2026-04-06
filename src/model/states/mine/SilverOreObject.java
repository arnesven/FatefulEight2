package model.states.mine;

import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;

public class SilverOreObject extends MineOreObject {
    private static final Sprite[] SPRITES = MineObject.makeOreSprites(MyColors.DARK_GRAY, MyColors.GRAY, MyColors.LIGHT_GRAY);

    public SilverOreObject(int richness) {
        super(SPRITES[richness * SPRITES.length/3 + MyRandom.randInt(SPRITES.length/3)]);
    }
}
