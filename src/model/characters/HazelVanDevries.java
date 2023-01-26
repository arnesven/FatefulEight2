package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.CharacterEyes;
import model.characters.appearance.HairStyle3x2;
import model.races.Race;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;

public class HazelVanDevries extends AdvancedAppearance {

    public HazelVanDevries() {
        super(Race.SOUTHERN_HUMAN, true, MyColors.DARK_BROWN,
                3, 2, new CharacterEyes(0xA, 0xB), new HairStyle3x2(9, true, 0x10), null);
    }

    @Override
    protected int getRightCheek() {
        return 0x47;
    }

    @Override
    protected int getLeftCheek() {
        return 0x37;
    }

    @Override
    protected void specialization() {
        setSprite(1, 4, new FaceSpriteWithHair(0xF0, getHairColor()));
        setSprite(5, 4, new FaceSpriteWithHair(0xF1, getHairColor()));
    }

    @Override
    public CharacterAppearance copy() {
        return new HazelVanDevries();
    }
}
