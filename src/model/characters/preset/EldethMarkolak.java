package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class EldethMarkolak extends AdvancedAppearance {
    public EldethMarkolak() {
        super(Race.DWARF, true, MyColors.BROWN,
                6, 6, new CharacterEyes(0xC, 0xD),
                new ShortFemaleHair("Eldeth"), new Beard(8, 0x00));
    }

}
