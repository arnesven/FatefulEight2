package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class LonnieLiebgott extends AdvancedAppearance {
    public LonnieLiebgott() {
        super(Race.NORTHERN_HUMAN, false, MyColors.BLACK,
                0, 0, new NormalBigEyes(), new BaldHairStyle(), null);
    }

    @Override
    public boolean hairInForehead() {
        return false;
    }

    @Override
    public CharacterAppearance copy() {
        return new LonnieLiebgott();
    }
}
