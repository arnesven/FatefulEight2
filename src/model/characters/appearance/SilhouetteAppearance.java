package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.FullPortraitSprite;
import view.sprites.Sprite;

public class SilhouetteAppearance extends CharacterAppearance {
    private static final Sprite SILHOUETTE_SPRITE = new FullPortraitSprite(0, 0);
    public SilhouetteAppearance() {
        super(Race.ALL, true, MyColors.BLACK);
    }

    @Override
    protected int getMouth() {
        return 0;
    }

    @Override
    protected int getEye() {
        return 0;
    }

    @Override
    public int getNose() {
        return 0;
    }

    @Override
    public boolean hairInForehead() {
        return false;
    }

    @Override
    public boolean hairOnTop() {
        return false;
    }

    @Override
    public CharacterAppearance copy() {
        return null;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.clearSpace(col, col+7, row, row+7);
        screenHandler.put(col, row, SILHOUETTE_SPRITE);
    }

}
