package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class IvanMcIntosh extends AdvancedAppearance {
    public IvanMcIntosh() {
        super(Race.NORTHERN_HUMAN, false, MyColors.GRAY_RED, CharacterCreationView.mouthSet[12],
                CharacterCreationView.noseSet[11], CharacterEyes.allEyes[0], new SpikesHairStyle(), Beard.allBeards[7]);
    }
}
