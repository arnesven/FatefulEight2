package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class RiboxAnari extends AdvancedAppearance {
    public RiboxAnari() {
        super(Race.WOOD_ELF, false, MyColors.TAN, CharacterCreationView.mouthSet[2],
                CharacterCreationView.noseSet[8], CharacterEyes.allEyes[5], new BigHairStyle(),
                Beard.allBeards[12]);
    }
}
