package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class BazUrGhan extends AdvancedAppearance {
    public BazUrGhan() {
        super(Race.HALF_ORC, false, MyColors.DARK_GRAY,
                CharacterCreationView.mouthSet[7],
                CharacterCreationView.noseSet[5],
                CharacterEyes.allEyes[0],
                new SpikesHairStyle(),
                Beard.allBeards[14]);
    }
}
