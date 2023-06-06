package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class BungoDarkwood extends AdvancedAppearance {
    public BungoDarkwood() {
        super(Race.HALFLING, false, MyColors.GRAY_RED, 3, 6, new CharacterEyes(0),
                new HairStyle3x2(0x167, true, true, false, false, 0x08, 0x00), null);
    }
}
