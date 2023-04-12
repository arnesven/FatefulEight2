package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.sprites.Sprite8x8;

public class KruskTalandro extends AdvancedAppearance {
    public KruskTalandro() {
        super(Race.HALF_ORC, false, MyColors.DARK_GRAY, 7, 2,
                new CharacterEyes(1), new BaldHairStyle(), new Beard(0x09, 0x00));
        setHasGlasses(true);
    }

    @Override
    protected void specialization() {
        super.specialization();
        getSprite(3, 4).setColor4(MyColors.BEIGE);
    }

    @Override
    public void applyFacialHair(Race race) {
        super.applyFacialHair(race);
    }

    @Override
    public CharacterAppearance copy() {
        return new KruskTalandro();
    }
}
