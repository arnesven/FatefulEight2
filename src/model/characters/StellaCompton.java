package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class StellaCompton extends AdvancedAppearance {
    public StellaCompton() {
        super(Race.HALFLING, true, MyColors.DARK_BROWN, CharacterCreationView.mouthSet[6],
                CharacterCreationView.noseSet[7], CharacterEyes.allEyes[6], HairStyle.allHairStyles[3], Beard.allBeards[0]);
        setMascaraColor(MyColors.DARK_PURPLE);
    }
}
