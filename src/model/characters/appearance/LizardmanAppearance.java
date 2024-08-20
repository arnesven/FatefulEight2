package model.characters.appearance;

import model.races.Race;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.FullPortraitSprite;

import java.util.List;

public class LizardmanAppearance extends CharacterAppearance {

    private static final List<MyColors> HAIR_COLORS = List.of(MyColors.GRAY, MyColors.GOLD,
            MyColors.GRAY_RED, MyColors.DARK_GREEN, MyColors.DARK_BLUE);
    private static final List<MyPair<MyColors, MyColors>> SKIN_COLOR_PAIRS =
            List.of(new MyPair<>(MyColors.DARK_GREEN, MyColors.GREEN),
                    new MyPair<>(MyColors.GOLD, MyColors.YELLOW),
                    new MyPair<>(MyColors.BLUE, MyColors.LIGHT_BLUE),
                    new MyPair<>(MyColors.DARK_RED, MyColors.ORANGE));
    private final LizardmanPortraitSprite baseSprite;

    public LizardmanAppearance() {
        super(Race.LIZARDMAN, false, MyColors.BLACK);
        MyColors hairColor = MyRandom.sample(HAIR_COLORS);
        MyPair<MyColors, MyColors> skinColorPair = MyRandom.sample(SKIN_COLOR_PAIRS);
        this.baseSprite = new LizardmanPortraitSprite(
                MyColors.DARK_BROWN, skinColorPair.first, MyColors.DARK_PURPLE,
                MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, skinColorPair.second, hairColor);
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.clearSpace(col, col+7, row, row+7);
        screenHandler.put(col, row, baseSprite);
        screenHandler.put(col, row, baseSprite);
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
        return new LizardmanAppearance();
    }

    private static class LizardmanPortraitSprite extends FullPortraitSprite {
        public LizardmanPortraitSprite(MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
            super(3, 0);
            setColor1(color1);
            setColor2(color2);
            setColor3(color3);
            setColor4(color4);
        }

        public LizardmanPortraitSprite(MyColors color1, MyColors color3, MyColors color4,
                                       MyColors color5, MyColors color6, MyColors color7, MyColors color8) {
            super(2, 0, List.of(new LizardmanPortraitSprite(color5, color6, color7, color8)));
            setColor1(color1);
            setColor3(color3);
            setColor4(color4);
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
