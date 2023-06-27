package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class EmilyFourhorn extends AdvancedAppearance {
    public EmilyFourhorn() {
        super(Race.NORTHERN_HUMAN, true, MyColors.RED, CharacterCreationView.mouthSet[2],
                CharacterCreationView.noseSet[2], CharacterEyes.allEyes[8], HairStyle.allHairStyles[30], Beard.allBeards[0]);
        setFaceDetail(new EarringsDetail());
        setDetailColor(MyColors.ORANGE);
    }
}
