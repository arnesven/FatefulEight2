package model.states.dailyaction.tavern;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

public class VikingTavernNode extends TavernNode {

    private static final Sprite SPRITE = new Sprite32x32("innlower", "world_foreground.png", 0x33,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.WHITE, MyColors.GOLD);
    private static final Sprite SPRITE2 = new Sprite32x32("innupper", "world_foreground.png", 0x23,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.WHITE, MyColors.GOLD);

    public VikingTavernNode(boolean freeLodging) {
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
