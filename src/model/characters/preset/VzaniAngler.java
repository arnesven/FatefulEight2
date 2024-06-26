package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.BaggyBigEyesWithBangs;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.races.Race;
import view.MyColors;

public class VzaniAngler extends AdvancedAppearance {
    public VzaniAngler() {
        super(Race.HALFLING, true, MyColors.WHITE,
                3, 4, new BaggyBigEyesWithBangs(),
                new VzaniHairStyle(),
                new Beard(3, 0x00));
    }

}
