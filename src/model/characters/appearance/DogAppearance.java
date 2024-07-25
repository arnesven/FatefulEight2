package model.characters.appearance;

import model.races.Race;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.FullPortraitSprite;
import view.sprites.Sprite;

public class DogAppearance extends CharacterAppearance {
    private static final Sprite SPRITE = new DogPortraitSprite();

    public DogAppearance() {
        super(Race.DOG, MyRandom.flipCoin(), MyColors.BEIGE);
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
        return new DogAppearance();
    }

    @Override
    public void drawBlink(ScreenHandler screenHandler, int x, int y) { }

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y) { }

    @Override
    public boolean supportsSpeakingAnimation() {
        return false;
    }

    private static class DogPortraitSprite extends FullPortraitSprite {
        public DogPortraitSprite() {
            super(0, 1);
            MyColors.transformImage(this);
        }
    }
}
