package model.characters.preset;

import model.characters.FemaleLongHairStyle;
import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class DeniseBoyd extends AdvancedAppearance {
    public DeniseBoyd() {
        super(Race.NORTHERN_HUMAN, true, MyColors.ORANGE, 1, 9, new CharacterEyes(2, 3),
                new FemaleLongHairStyle("Denise"), null);
        setMascaraColor(MyColors.LIGHT_RED);
        setLipColor(MyColors.RED);
    }

}
