package model.states.dailyaction.tavern;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

public class EasternTavernNode extends TavernNode {

    private static final Sprite SPRITE = new Sprite32x32("innlower", "world_foreground.png", 0x33,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.RED, MyColors.WHITE);
    private static final Sprite SPRITE2 = new Sprite32x32("innupper", "world_foreground.png", 0x23,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.RED, MyColors.WHITE);

    public EasternTavernNode(boolean freeLodge) {
        super(freeLodge);
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
