package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.BaggySmallEyes;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.HairStyle3x2;
import model.races.Race;
import view.MyColors;

public class LeodorSunshadow extends AdvancedAppearance {
    public LeodorSunshadow() {
        super(Race.DARK_ELF, false, MyColors.WHITE, 1, 0, new BaggySmallEyes(),
                new HairStyle3x2(0xC, false, 0x17, 0x32, 0x0, "Leodor"), null);
    }
}
