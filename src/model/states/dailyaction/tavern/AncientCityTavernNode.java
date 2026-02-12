package model.states.dailyaction.tavern;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class AncientCityTavernNode extends TavernNode {

    private static final Sprite SPRITE = new Sprite32x32("innlower", "world_foreground.png", 0xF2,
            MyColors.BLACK, MyColors.PEACH, MyColors.DARK_RED, MyColors.BROWN);
    private static final Sprite SPRITE2 = new Sprite32x32("innupper", "world_foreground.png", 0xE2,
            MyColors.BLACK, MyColors.PEACH, MyColors.DARK_RED, MyColors.BROWN);

    public AncientCityTavernNode(boolean freeLodging) {
        super(freeLodging);
    }

    @Override
    protected Sprite getBackgroundSpriteTop() {
        return SPRITE2;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }
}
