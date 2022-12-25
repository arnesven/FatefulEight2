package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.races.Race;
import view.MyColors;

public class ThorbaltRamcrown extends AdvancedAppearance {
    public ThorbaltRamcrown() {
        super(Race.DWARF, false, MyColors.BROWN,
                4, 5, new CharacterEyes(6, 7), null, new Beard(4));
    }

}
