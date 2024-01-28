package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class MuldanEbonclaw extends AdvancedAppearance {
    public MuldanEbonclaw() {
        super(Race.DARK_ELF, false, MyColors.GRAY_RED, CharacterCreationView.mouthSet[12],
                CharacterCreationView.noseSet[6], CharacterEyes.allEyes[1], HairStyle.allHairStyles[28], Beard.allBeards[17]);
    }
}
