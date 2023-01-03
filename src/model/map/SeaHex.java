package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.NoEventState;
import view.subviews.SubView;
import view.MyColors;
import view.sprites.HexSprite;
import view.sprites.SeaHexSprite;

public class SeaHex extends WorldHex {
    public SeaHex() {
        super(MyColors.LIGHT_BLUE, 0, ALL, null);
    }

    @Override
    protected HexSprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return new SeaHexSprite(0x40);
    }

    @Override
    protected HexSprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return new SeaHexSprite(0x41);
    }

    @Override
    protected HexSprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return new SeaHexSprite(0x50);
    }

    @Override
    protected HexSprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return new SeaHexSprite(0x51);
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
