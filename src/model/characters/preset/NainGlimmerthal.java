package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class NainGlimmerthal extends AdvancedAppearance {
    public NainGlimmerthal() {
        super(Race.DWARF, false, MyColors.ORANGE, 5, 6,
                new BaggySmallEyesWithSideburns(), new BaldHairStyle(), new BraidedBeard(MyColors.DARK_RED, MyColors.DARK_BLUE));
    }
}
