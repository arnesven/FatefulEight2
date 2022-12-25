package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.BigBeard;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.TopKnotHairStyle;
import model.races.Race;
import view.MyColors;

public class JordynStrong extends AdvancedAppearance {
    public JordynStrong() {
        super(Race.SOUTHERN_HUMAN, false, MyColors.DARK_BROWN,
                4, 8, new CharacterEyes(1),
                new TopKnotHairStyle(MyColors.BEIGE, true), new BigBeard(MyColors.DARK_BROWN));
    }
}
