package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;

public class HazelVanDevries extends AdvancedAppearance {

    public HazelVanDevries() {
        super(Race.SOUTHERN_HUMAN, true, MyColors.DARK_BROWN,
                3, 2, new SmallEyesWithBangsRight(),
                new HairStyle3x2(9, true, 0x10, 0x32, 0x0, "Hazel"), null);
        setMascaraColor(MyColors.DARK_GREEN);
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
