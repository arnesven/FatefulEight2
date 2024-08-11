package model.races;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.FullPortraitSprite;
import view.sprites.Sprite;

public class FrogmanAppearance extends CharacterAppearance {

    private static final Sprite SPRITE = new FrogmanPortraitSprite();

    public FrogmanAppearance() {
        super(Race.FROGMAN, false, MyColors.BLACK);
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.clearSpace(col, col+7, row, row+7);
        screenHandler.put(col, row, SPRITE);
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
        return new FrogmanAppearance();
    }

    private static class FrogmanPortraitSprite extends FullPortraitSprite {
        public FrogmanPortraitSprite() {
            super(1, 1);
            setColor1(MyColors.BLACK);
            setColor3(MyColors.BROWN);
            setColor4(MyColors.DARK_GREEN);
        }
    }
}
