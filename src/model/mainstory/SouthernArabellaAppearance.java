package model.mainstory;

import model.characters.appearance.*;
import model.classes.Classes;
import model.races.Race;
import view.MyColors;

public class SouthernArabellaAppearance extends AdvancedAppearance {
    public SouthernArabellaAppearance() {
        super(Race.SOUTHERN_HUMAN, true, MyColors.DARK_RED, 2, 0x18, new ElfinEyes(),
                new HeartHairStyle(new PuyetHairStyle(), true, true, "Heart"),
                new Beard(0xD, 0x00, false));
        setMascaraColor(MyColors.BROWN);
        setClass(new ArabellaClass());
    }
}
