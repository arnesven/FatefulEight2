package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class PaddyWillowbrush extends AdvancedAppearance {
    public PaddyWillowbrush() {
        super(Race.HALFLING, false, MyColors.BROWN, 0, 2, new CharacterEyes(0),
                new HairStyle3x2(0xC, false, 0x17), new Beard(0xA));
    }
}
