package model.characters.special;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class GrumbledookAppearance extends AdvancedAppearance {

    public GrumbledookAppearance() {
        super(Race.HIGH_ELF, false, MyColors.GOLD,
                5, 6, CharacterEyes.allEyes[7], new WildSideHairStyle(), Beard.allBeards[22]);

    }
}
