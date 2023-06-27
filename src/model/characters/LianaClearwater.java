package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class LianaClearwater extends AdvancedAppearance {
    public LianaClearwater() {
        super(Race.WOOD_ELF, true, MyColors.GOLD, CharacterCreationView.mouthSet[8],
                CharacterCreationView.noseSet[9], CharacterEyes.allEyes[6], HairStyle.allHairStyles[26], Beard.allBeards[11]);
        setFaceDetail(new EarringsDetail());
        setDetailColor(MyColors.CYAN);
    }
}
