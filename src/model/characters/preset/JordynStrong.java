package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class JordynStrong extends AdvancedAppearance {
    public JordynStrong() {
        super(Race.SOUTHERN_HUMAN, false, MyColors.DARK_BROWN,
                4, 8, new NormalSmallEyes(),
                new TopKnotHairStyle(MyColors.BEIGE, true, "Jordyn"), new BigBeard(MyColors.DARK_BROWN));
    }
}
