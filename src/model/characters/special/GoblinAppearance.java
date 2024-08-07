package model.characters.special;

import model.characters.appearance.CharacterAppearance;
import model.races.Race;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.FullPortraitSprite;
import view.sprites.Sprite;

public class GoblinAppearance extends CharacterAppearance {
    private static final Sprite SPRITE = new GoblinPortraitSprite();

    public GoblinAppearance() {
        super(Race.GOBLIN, false, MyColors.PEACH);
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
        return new GoblinAppearance();
    }

    private static class GoblinPortraitSprite extends FullPortraitSprite {
        public GoblinPortraitSprite() {
            super(1, 0);
            setColor1(MyColors.ORC_GREEN);
            setColor3(MyColors.YELLOW);
            setColor4(MyColors.TAN);
        }
    }

    @Override
    public void drawBlink(ScreenHandler screenHandler, int x, int y) { }

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y) { }

    @Override
    public boolean supportsSpeakingAnimation() {
        return false;
    }
}
