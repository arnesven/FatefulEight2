package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class HurinHammerfall extends AdvancedAppearance {
    public HurinHammerfall() {
        super(Race.DWARF, false, MyColors.DARK_GRAY,
                CharacterCreationView.mouthSet[4],
                CharacterCreationView.noseSet[11],
                CharacterEyes.allEyes[1],
                new WavyHairStyle(),
                Beard.allBeards[16]);
    }
}
