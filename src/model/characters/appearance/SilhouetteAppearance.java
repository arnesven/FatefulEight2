package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;

public class SilhouetteAppearance extends CharacterAppearance {
    private static final Sprite SILHOUETTE_SPRITE = new SilhouetteSprite();
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

    private static class SilhouetteSprite extends Sprite {
        public SilhouetteSprite() {
            super("silhouette", "silhouette.png", 0, 0, 56, 56);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.LIGHT_GRAY);
            setColor3(MyColors.GRAY);
        }
    }

}
