package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class EmilyFourhorn extends AdvancedAppearance {
    public EmilyFourhorn() {
        super(Race.NORTHERN_HUMAN, true, MyColors.RED, CharacterCreationView.mouthSet[2],
                CharacterCreationView.noseSet[2], CharacterEyes.allEyes[8], new BunsHairStyle(), Beard.allBeards[0]);
        addFaceDetail(new EarringsDetail());
        setDetailColor(MyColors.ORANGE);
        setMascaraColor(MyColors.LIGHT_GRAY);
    }
}
