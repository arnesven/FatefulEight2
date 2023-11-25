package model.characters;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class MialeeSeverin extends AdvancedAppearance {
    public MialeeSeverin() {
        super(Race.DARK_ELF, true, MyColors.BROWN,
                2, 3, new CharacterEyes(2, 3),
                new ExplicitHairStyle(true, 0x04, 0xEF, 0x14, 0x05, 0xFC, 0x15, 0x10, 0x00),
                new Beard(2, 0x00));
        setLipColor(MyColors.DARK_BLUE);
    }

    @Override
    protected boolean classSpecificEars() {
        return false;
    }

    @Override
    public PortraitSprite getLeftEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x73, hairColor);
    }

    @Override
    public PortraitSprite getRightEar(MyColors hairColor) {
        return new FaceSpriteWithHair(0x83, hairColor);
    }

    @Override
    public CharacterAppearance copy() {
        return new MialeeSeverin();
    }
}
