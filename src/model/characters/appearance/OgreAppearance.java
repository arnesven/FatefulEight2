package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.FullPortraitSprite;
import view.sprites.GigantoidBaseSprite;
import view.sprites.Sprite;

public class OgreAppearance extends GigantoidAppearance {

    private static final Sprite OGRE_OVERLAY = new OgreOverlaySprite();
    private static final GigantoidBaseSprite BASE_SPRITE = new GigantoidBaseSprite(Race.OGRE, MyColors.BEIGE,
            MyColors.BROWN, MyColors.BLACK, OGRE_OVERLAY);

    public OgreAppearance() {
        super(Race.OGRE);
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.clearSpace(col, col+7, row, row+7);
        screenHandler.put(col, row, BASE_SPRITE);
    }

    @Override
    public CharacterAppearance copy() {
        return new OgreAppearance();
    }

    private static class OgreOverlaySprite extends FullPortraitSprite {
        public OgreOverlaySprite() {
            super(2, 2);
            setColor1(MyColors.BLACK);
            setColor3(Race.OGRE.getColor());
            setColor4(MyColors.GOLD);
        }
    }
}
