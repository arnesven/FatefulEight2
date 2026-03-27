package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.FullPortraitSprite;
import view.sprites.GigantoidBaseSprite;
import view.sprites.Sprite;

public class GiantAppearance extends GigantoidAppearance {

    private static final Sprite GIANT_OVERLAY = new GiantOverlaySprite();
    private static final GigantoidBaseSprite BASE_SPRITE = new GigantoidBaseSprite(Race.GIANT,
            MyColors.WHITE, MyColors.LIGHT_BLUE, MyColors.BLACK, GIANT_OVERLAY);

    public GiantAppearance() {
        super(Race.GIANT);
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.clearSpace(col, col+7, row, row+7);
        screenHandler.put(col, row, BASE_SPRITE);
    }

    @Override
    public CharacterAppearance copy() {
        return new GiantAppearance();
    }

    private static class GiantOverlaySprite extends FullPortraitSprite {
        public GiantOverlaySprite() {
            super(3, 2);
            setColor1(MyColors.BLACK);
            setColor3(Race.GIANT.getColor());
            setColor4(MyColors.DARK_BROWN);
        }
    }
}
