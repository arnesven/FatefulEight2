package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.races.Race;
import view.MyColors;

public class VzaniAngler extends AdvancedAppearance {
    public VzaniAngler() {
        super(Race.HALFLING, true, MyColors.WHITE,
                3, 4, new CharacterEyes(4, 5),
                new VzaniHairStyle(),
                new Beard(3));
    }

}
