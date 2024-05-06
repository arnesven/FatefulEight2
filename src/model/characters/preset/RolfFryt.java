package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class RolfFryt extends AdvancedAppearance {
    public RolfFryt() {
        super(Race.NORTHERN_HUMAN, false, MyColors.BROWN,
                5, 6, new NormalSmallEyes(), new HairStyle3x2(6, false, 0x16, 0x16, 0x0, "Rolf"), new Beard(5, 0x41));
    }
}
