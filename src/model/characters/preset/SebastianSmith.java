package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class SebastianSmith extends AdvancedAppearance {
    public SebastianSmith() {
        super(Race.SOUTHERN_HUMAN, false, MyColors.DARK_GRAY, CharacterCreationView.mouthSet[12],
                CharacterCreationView.noseSet[1], CharacterEyes.allEyes[1], HairStyle.allHairStyles[28], Beard.allBeards[8]);
    }
}
