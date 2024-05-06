package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;

public class GorgaBonecrag extends AdvancedAppearance {
    public GorgaBonecrag() {
        super(Race.HALF_ORC, true, MyColors.DARK_GRAY, 7, 8, new NormalSmallEyes(),
                new HairStyle3x2(0x9D, false, 0x18, 0x18, 0x0, "Gorga"), null);
        setMascaraColor(MyColors.CYAN);
    }

    @Override
    public CharacterAppearance copy() {
        return new GorgaBonecrag();
    }

    @Override
    protected void specialization() {
        super.specialization();
        getSprite(3, 4).setColor4(MyColors.BEIGE);
    }
}
