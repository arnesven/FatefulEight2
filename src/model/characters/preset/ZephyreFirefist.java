package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class ZephyreFirefist extends AdvancedAppearance {
    public ZephyreFirefist() {
        super(Race.DARK_ELF, true, MyColors.DARK_RED, CharacterCreationView.mouthSet[11],
                CharacterCreationView.noseSet[5], CharacterEyes.allEyes[6],
                new HairStyle3x2(0xA7, false, true, true, true, 0x05, 0x07, 0x33, 0x34, "Rough/Long"),
                Beard.allBeards[10]);
        setLipColor(MyColors.DARK_BROWN);
    }
}
