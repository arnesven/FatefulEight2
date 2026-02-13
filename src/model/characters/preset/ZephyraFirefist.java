package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class ZephyraFirefist extends AdvancedAppearance {
    public ZephyraFirefist() {
        super(Race.DARK_ELF, true, MyColors.DARK_RED, CharacterCreationView.mouthSet[11],
                CharacterCreationView.noseSet[5], CharacterEyes.allEyes[6],
                new RoughLongHairStyle(),
                Beard.allBeards[10]);
        setLipColor(MyColors.DARK_BROWN);
        addFaceDetail(new RougeDetail());
        setDetailColor(MyColors.GRAY_RED);
    }
}
