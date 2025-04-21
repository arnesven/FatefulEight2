package model.mainstory.honorable;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.MikosBeard;
import model.characters.appearance.OldManHairStyle;
import model.classes.CharacterClass;
import model.classes.NoClass;
import model.races.AllRaces;
import view.MyColors;
import view.ScreenHandler;

public class MikoAppearance extends AdvancedAppearance {

    public MikoAppearance() {
        super(AllRaces.EASTERN_HUMAN, false, MyColors.WHITE, 0xE, 0xE, new BlindEasternEyes(),
                new OldManHairStyle(), new MikosBeard(MyColors.BLACK));
        setLipColor(MyColors.DARK_GRAY);
        CharacterClass grayNoneClass = new NoClass(MyColors.GRAY_RED);
        setClass(grayNoneClass);
    }

    @Override
    public void drawBlink(ScreenHandler screenHandler, int x, int y) { }

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y) { }

    private static class BlindEasternEyes extends CharacterEyes {
        public BlindEasternEyes() {
            super(0x1FD, 0x1FE, "", 0);
        }
    }
}
