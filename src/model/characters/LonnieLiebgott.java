package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterEyes;
import model.races.Race;
import view.MyColors;

public class LonnieLiebgott extends AdvancedAppearance {
    public LonnieLiebgott() {
        super(Race.NORTHERN_HUMAN, false, MyColors.BLACK,
                0, 0, new CharacterEyes(0), null, null);
    }

    @Override
    public boolean hairInForehead() {
        return false;
    }
}
