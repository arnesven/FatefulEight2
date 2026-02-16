package model.mainstory.vikings;

import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.NoClass;
import model.races.HunkyShoulders;
import model.races.Race;
import view.MyColors;
import view.ScreenHandler;

public class OldElleAppearance extends AdvancedAppearance {
    public OldElleAppearance() {
        super(Race.DARK_ELF, true, MyColors.WHITE, 2, 2,
                new BlindEasternEyes(), new ShortFemaleHair("Short"), new NoBeard());
        setShoulders(new HunkyShoulders(true));
        setNeck(new HunkyNeck());
        setMascaraColor(MyColors.DARK_GRAY);
        CharacterClass grayNoneClass = new NoClass(MyColors.GRAY_RED);
        setClass(grayNoneClass);
    }

    @Override
    public void drawBlink(ScreenHandler screenHandler, int x, int y) { }

    @Override
    public void drawDrawLook(ScreenHandler screenHandler, boolean left, int x, int y, boolean leftEye, boolean rightEye) { }
}
