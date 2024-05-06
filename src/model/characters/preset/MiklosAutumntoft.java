package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.ExplicitHairStyle;
import model.races.Race;
import view.MyColors;

public class MiklosAutumntoft extends AdvancedAppearance {
    public MiklosAutumntoft() {
        super(Race.HIGH_ELF, false, MyColors.LIGHT_GRAY,
                1, 2, new CharacterEyes(1),
                new ExplicitHairStyle(false, 0x02, 0xFF, 0x12,
                        0x03, 0xFD, 0x13, 0x20, 0x00, 0x32, 0x0,
                        "Miklos"), null);
    }

}
