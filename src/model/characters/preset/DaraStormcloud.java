package model.characters.preset;

import model.characters.appearance.*;
import model.races.Race;
import view.MyColors;
import view.sprites.FaceSpriteWithHair;
import view.sprites.PortraitSprite;

public class DaraStormcloud extends AdvancedAppearance {
    public DaraStormcloud() {
        super(Race.WOOD_ELF, true, MyColors.BROWN, 2, 9, new SmallEyesWithBangs(),
                new HairStyle3x2(0x164, true, true, true, true, 0x09, 0x07, 0x33, 0x34, "Dara"), null);
        setMascaraColor(MyColors.LIGHT_GREEN);
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

}
