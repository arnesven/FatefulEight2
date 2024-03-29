package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class MelethainGauth extends AdvancedAppearance {
    public MelethainGauth() {
        super(Race.HIGH_ELF, false, MyColors.PEACH, CharacterCreationView.mouthSet[4],
                CharacterCreationView.noseSet[6], CharacterEyes.allEyes[7], new WavyHairStyle(), Beard.allBeards[11]);
    }
}
