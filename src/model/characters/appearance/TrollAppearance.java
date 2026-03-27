package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

public class TrollAppearance extends GigantoidAppearance {

    private static final TrollOverlaySprite TROLL_OVERLAY = new TrollOverlaySprite();
    private static final GigantoidBaseSprite BASE_SPRITE = new GigantoidBaseSprite(Race.TROLL,
            MyColors.LIGHT_YELLOW, MyColors.LIGHT_YELLOW, MyColors.BLACK, TROLL_OVERLAY);

    public TrollAppearance() {
        super(Race.TROLL);
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.clearSpace(col, col+7, row, row+7);
        screenHandler.put(col, row, BASE_SPRITE);
    }

    @Override
    public CharacterAppearance copy() {
        return new TrollAppearance();
    }

    private static class TrollOverlaySprite extends FullPortraitSprite {
        public TrollOverlaySprite() {
            super(1, 2);
            setColor1(MyColors.BLACK);
            setColor3(Race.TROLL.getColor());
            setColor4(MyColors.BEIGE);
        }
    }
}
