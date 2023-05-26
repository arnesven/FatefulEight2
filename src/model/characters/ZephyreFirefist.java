package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class ZephyreFirefist extends AdvancedAppearance {
    public ZephyreFirefist() {
        super(Race.DARK_ELF, true, MyColors.DARK_RED, CharacterCreationView.mouthSet[11],
                CharacterCreationView.noseSet[5], CharacterEyes.allEyes[6], HairStyle.allHairStyles[32], Beard.allBeards[10]);
    }
}
