package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class MordKroft extends AdvancedAppearance {
    public MordKroft() {
        super(Race.HALF_ORC, false, MyColors.DARK_GRAY, 7, 0, new SmallEyesWithBangsRight(),
                new MordKroftHairStyle("Mord"), new Beard(9, 0x00));
    }

    @Override
    protected void specialization() {
        super.specialization();
        getSprite(3, 4).setColor4(MyColors.BEIGE);
    }

    @Override
    public CharacterAppearance copy() {
        return new MordKroft();
    }
}
