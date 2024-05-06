package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class ThorbaltRamcrown extends AdvancedAppearance {
    public ThorbaltRamcrown() {
        super(Race.DWARF, false, MyColors.BROWN,
                4, 5, new SmallEyesWithSideburns(), new BaldHairStyle(), new Beard(4, 0x40));
    }

}
