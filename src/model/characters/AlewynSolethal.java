package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class AlewynSolethal extends AdvancedAppearance {
    public AlewynSolethal() {
        super(Race.HIGH_ELF, true, MyColors.GRAY, CharacterCreationView.mouthSet[6],
                CharacterCreationView.noseSet[4], CharacterEyes.allEyes[2], HairStyle.allHairStyles[31], Beard.allBeards[11]);
        setHasGlasses(true);
        setHasEarrings(true);
        setDetailColor(MyColors.GOLD);
    }
}
