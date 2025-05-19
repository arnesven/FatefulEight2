package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class LianaClearwater extends AdvancedAppearance {
    public LianaClearwater() {
        super(Race.WOOD_ELF, true, MyColors.GOLD, CharacterCreationView.mouthSet[8],
                CharacterCreationView.noseSet[9], CharacterEyes.allEyes[6],
                new PigTailHairStyle(0x9, true, 0x26, "Braids/Flat"),
                Beard.allBeards[11]);
        addFaceDetail(new TriangularEarringsDetail());
        setDetailColor(MyColors.BLUE);
        setMascaraColor(MyColors.LIGHT_RED);
    }
}
