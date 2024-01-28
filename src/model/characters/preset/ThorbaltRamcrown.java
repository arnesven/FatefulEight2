package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.BaldHairStyle;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.races.Race;
import view.MyColors;

public class ThorbaltRamcrown extends AdvancedAppearance {
    public ThorbaltRamcrown() {
        super(Race.DWARF, false, MyColors.BROWN,
                4, 5, new CharacterEyes(6, 7), new BaldHairStyle(), new Beard(4, 0x40));
    }

}
