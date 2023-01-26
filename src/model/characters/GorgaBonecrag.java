package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.HairStyle3x2;
import model.races.Race;
import view.MyColors;

public class GorgaBonecrag extends AdvancedAppearance {
    public GorgaBonecrag() {
        super(Race.HALF_ORC, true, MyColors.DARK_GRAY, 7, 8, new CharacterEyes(1),
                new HairStyle3x2(0x9D, false, 0x18), null);
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
