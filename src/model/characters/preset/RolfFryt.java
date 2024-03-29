package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.HairStyle3x2;
import model.races.Race;
import view.MyColors;

public class RolfFryt extends AdvancedAppearance {
    public RolfFryt() {
        super(Race.NORTHERN_HUMAN, false, MyColors.BROWN,
                5, 6, new CharacterEyes(1), new HairStyle3x2(6, false, 0x16, "Rolf"), new Beard(5, 0x41));
    }
}
