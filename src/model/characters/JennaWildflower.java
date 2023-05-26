package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class JennaWildflower extends AdvancedAppearance {
    public JennaWildflower() {
        super(Race.SOUTHERN_HUMAN, true, MyColors.LIGHT_GRAY, CharacterCreationView.mouthSet[1],
                CharacterCreationView.noseSet[3], CharacterEyes.allEyes[3], HairStyle.allHairStyles[31], Beard.allBeards[8]);
        setHasEarrings(true);
        setDetailColor(MyColors.WHITE);
    }
}
