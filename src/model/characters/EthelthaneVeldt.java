package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class EthelthaneVeldt extends AdvancedAppearance {
    public EthelthaneVeldt() {
        super(Race.DWARF, true, MyColors.BROWN, CharacterCreationView.mouthSet[11],
                CharacterCreationView.noseSet[11], CharacterEyes.allEyes[0], HairStyle.allHairStyles[20], Beard.allBeards[12]);
        setHasGlasses(true);
        setDetailColor(MyColors.CYAN);
    }
}