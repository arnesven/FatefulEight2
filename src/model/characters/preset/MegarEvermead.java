package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class MegarEvermead extends AdvancedAppearance {
    public MegarEvermead() {
        super(Race.DWARF, false, MyColors.WHITE,
                4, 9, new SmallEyesWithSideburns(),
                new TopKnotHairStyle(MyColors.GRAY, true, "Megar"), new BigAndLongBeard(MyColors.GRAY));
    }
}
