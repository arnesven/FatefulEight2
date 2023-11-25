package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class VendelaGawains extends AdvancedAppearance {
    public VendelaGawains() {
        super(Race.HALF_ORC, true, MyColors.DARK_GRAY, CharacterCreationView.mouthSet[7],
                CharacterCreationView.noseSet[7], CharacterEyes.allEyes[4], HairStyle.allHairStyles[11], Beard.allBeards[0]);
        setLipColor(MyColors.RED);
        setMascaraColor(MyColors.DARK_GREEN);
    }
}
