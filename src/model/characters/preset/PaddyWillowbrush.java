package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class PaddyWillowbrush extends AdvancedAppearance {
    public PaddyWillowbrush() {
        super(Race.HALFLING, false, MyColors.BROWN, 0, 2, new CharacterEyes(0),
                new HairStyle3x2(0xC, false, 0x17, 0x32, 0x0, "Paddy"), new Beard(0xA, 0x00));
    }
}
