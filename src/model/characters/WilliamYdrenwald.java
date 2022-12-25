package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterEyes;
import model.races.Race;
import view.MyColors;

public class WilliamYdrenwald extends AdvancedAppearance {
    public WilliamYdrenwald() {
        super(Race.NORTHERN_HUMAN, false, MyColors.WHITE,
               5, 7, new CharacterEyes(8, 9), null, new Beard(6));
    }

    @Override
    public boolean hairInForehead() {
        return false;
    }

}
