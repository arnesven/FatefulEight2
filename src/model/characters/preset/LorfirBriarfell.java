package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class LorfirBriarfell extends AdvancedAppearance {
    public LorfirBriarfell() {
        super(Race.WOOD_ELF, false, MyColors.DARK_BROWN, 0, 5, new NormalSmallEyes(),
                new HairStyle3x2(0xC, false, true, true, true, 0x27, 0x07, 0x33, 0x34, "Lorfir"), null);
    }
}
