package model.characters.appearance;

import view.sprites.FaceSpriteWithHair;

public class ShortFemaleHair extends HairStyle3x2 {
    public ShortFemaleHair() {
        super(0x9, true, true, true, false);
    }

    public ShortFemaleHair(int num) {
        super(num, true, true, true, false);
    }

    @Override
    public void apply(AdvancedAppearance appearance) {
        appearance.setSprite(1, 4, new FaceSpriteWithHair(0xF0, appearance.getHairColor()));
        appearance.setSprite(5, 4, new FaceSpriteWithHair(0xF1, appearance.getHairColor()));
    }
}
