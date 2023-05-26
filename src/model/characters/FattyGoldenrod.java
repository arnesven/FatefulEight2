package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.HairStyle;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class FattyGoldenrod extends AdvancedAppearance {
    public FattyGoldenrod() {
        super(Race.HALFLING, false, MyColors.DARK_BROWN, CharacterCreationView.mouthSet[5],
                CharacterCreationView.noseSet[6], CharacterEyes.allEyes[5], HairStyle.allHairStyles[34], Beard.allBeards[15]);
    }
}
