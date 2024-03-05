package model.characters.appearance;

import view.sprites.FaceSpriteWithHair;

public class ShortFemaleHair extends HairStyle3x2 {
    public ShortFemaleHair(String description) {
        super(0x9, true, true, true, false, 0x11, 0x12, description);
    }

    public ShortFemaleHair(int num, int normalAvatar, String description) {
        super(num, true, true, true, false, normalAvatar, 0x12, description);
    }

    @Override
    public void apply(AdvancedAppearance appearance) {
        appearance.setSprite(1, 4, new FaceSpriteWithHair(0xF0, appearance.getHairColor()));
        appearance.setSprite(5, 4, new FaceSpriteWithHair(0xF1, appearance.getHairColor()));
    }
}
