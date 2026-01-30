package model.mainstory;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class DarkElfArabellaAppearance extends AdvancedAppearance {
    public DarkElfArabellaAppearance() {
        super(Race.DARK_ELF, true, MyColors.DARK_RED, 9, 0x1EF,
                new SmallEyesWithBangs(), new SpecialMordKroftHairStyle(), new NoBeard());
        setClass(new ArabellaClass());
    }
}
