package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class VendelaGawains extends AdvancedAppearance {
    public VendelaGawains() {
        super(Race.HALF_ORC, true, MyColors.DARK_GRAY, CharacterCreationView.mouthSet[7],
                CharacterCreationView.noseSet[7], CharacterEyes.allEyes[4],
                new HairStyle3x2(0x164, true, true, true, true, 0x09, 0x07, 0x33, 0x34, "Combed/Long"),
                Beard.allBeards[0]);
        setLipColor(MyColors.RED);
        setMascaraColor(MyColors.DARK_GREEN);
    }
}
