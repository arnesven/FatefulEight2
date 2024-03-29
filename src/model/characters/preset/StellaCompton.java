package model.characters.preset;

import model.characters.FemaleLongHairStyle;
import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class StellaCompton extends AdvancedAppearance {
    public StellaCompton() {
        super(Race.HALFLING, true, MyColors.DARK_BROWN,
                CharacterCreationView.mouthSet[6],
                CharacterCreationView.noseSet[7],
                CharacterEyes.allEyes[6],
                new HeartHairStyle(new FemaleLongHairStyle("Long #1"), true, true, "Long #5"),
                Beard.allBeards[0]);
        setMascaraColor(MyColors.DARK_PURPLE);
    }
}
