package model.map;

import model.Model;
import model.states.DailyEventState;
import view.MyColors;
import view.sprites.HexSprite;
import view.subviews.SubView;

public class SolidRockHex extends WorldHex {
    public SolidRockHex() {
        super(MyColors.BLACK, 0, 0, null);
    }
    @Override
    protected HexSprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return new SolidRockHexSprite();
    }

    @Override
    protected HexSprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return new SolidRockHexSprite();
    }

    @Override
    protected HexSprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return new SolidRockHexSprite();
    }

    @Override
    protected HexSprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return new SolidRockHexSprite();
    }

    @Override
    public String getTerrainName() {
        return "Solid rock";
    }

    @Override
    public boolean canTravelTo(Model model) {
        return false;
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        throw new IllegalStateException("Should never be called!");
    }

    @Override
    protected SubView getSubView() {
        throw new IllegalStateException("Should never be called");
    }

    private static class SolidRockHexSprite extends HexSprite {
        public SolidRockHexSprite() {
            super("solidrock", 0x00, MyColors.BLACK);
        }
    }
}
