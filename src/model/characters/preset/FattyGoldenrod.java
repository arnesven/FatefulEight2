package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class FattyGoldenrod extends AdvancedAppearance {
    public FattyGoldenrod() {
        super(Race.HALFLING, false, MyColors.DARK_BROWN, CharacterCreationView.mouthSet[5],
                CharacterCreationView.noseSet[6], CharacterEyes.allEyes[5], new MessyHairStyle(), Beard.allBeards[15]);
    }
}
