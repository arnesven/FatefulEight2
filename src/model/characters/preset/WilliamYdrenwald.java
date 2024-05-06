package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class WilliamYdrenwald extends AdvancedAppearance {
    public WilliamYdrenwald() {
        super(Race.NORTHERN_HUMAN, false, MyColors.WHITE,
               5, 7, new BaggySmallEyesWithSideburns(), new BaldHairStyle(), new Beard(6, 0x42));
    }

    @Override
    public boolean hairInForehead() {
        return false;
    }

    @Override
    public CharacterAppearance copy() {
        return new WilliamYdrenwald();
    }
}
