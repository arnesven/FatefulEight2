package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class ZhandraMerkatys extends AdvancedAppearance {
    public ZhandraMerkatys() {
        super(Race.SOUTHERN_HUMAN, true, MyColors.DARK_RED, 1, 0xA, new CharacterEyes(0),
                new HairStyle3x2(0x5D, true, true, true, false, 0x19, 0x00), null);
    }

    @Override
    protected void specialization() {
        super.specialization();
        getSprite(3, 4).setColor2(MyColors.RED);
    }

    @Override
    public CharacterAppearance copy() {
        return new ZhandraMerkatys();
    }
}
