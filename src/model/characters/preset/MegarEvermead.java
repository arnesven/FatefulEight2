package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.BigAndLongBeard;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.TopKnotHairStyle;
import model.races.Race;
import view.MyColors;

public class MegarEvermead extends AdvancedAppearance {
    public MegarEvermead() {
        super(Race.DWARF, false, MyColors.WHITE,
                4, 9, new CharacterEyes(6,7),
                new TopKnotHairStyle(MyColors.GRAY, true, "Megar"), new BigAndLongBeard(MyColors.GRAY));
    }
}
