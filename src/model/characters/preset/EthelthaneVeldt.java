package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class EthelthaneVeldt extends AdvancedAppearance {
    public EthelthaneVeldt() {
        super(Race.DWARF, true, MyColors.BROWN,
                CharacterCreationView.mouthSet[11],
                CharacterCreationView.noseSet[11],
                CharacterEyes.allEyes[0],
                new SpecialMordKroftHairStyle(),
                Beard.allBeards[12]);
        addFaceDetail(new GlassesDetail());
        setDetailColor(MyColors.CYAN);
    }
}
