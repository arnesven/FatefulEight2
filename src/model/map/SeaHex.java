package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.NoEventState;
import view.sprites.Sprite;
import view.subviews.SubView;
import view.MyColors;
import view.sprites.HexSprite;
import view.sprites.SeaHexSprite;

public class SeaHex extends WorldHex {
    private static final SeaHexSprite SPRITE_UL = new SeaHexSprite(0xA0);
    private static final SeaHexSprite SPRITE_UR = new SeaHexSprite(0xA4);
    private static final SeaHexSprite SPRITE_LL = new SeaHexSprite(0xB0);
    private static final SeaHexSprite SPRITE_LR = new SeaHexSprite(0xB4);

    public SeaHex(int state) {
        super(MyColors.LIGHT_BLUE, 0, Direction.ALL, null, state);
    }

    @Override
    protected Sprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return SPRITE_UL;
    }

    @Override
    protected Sprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return SPRITE_UR;
    }

    @Override
    protected Sprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return SPRITE_LL;
    }

    @Override
    protected Sprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return SPRITE_LR;
    }

    @Override
    public String getTerrainName() {
        return "water";
    }

    public String getDescription() {
        return getTerrainName();
    }

    @Override
    protected SubView getSubView() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        return new NoEventState(model);
    }

    @Override
    public boolean canTravelTo(Model model) {
        return false;
    }
}
