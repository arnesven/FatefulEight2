package model.characters.appearance;

import view.sprites.FaceSpriteWithHair;

public class StylishHairStyle extends HairStyle3x2 {
    public StylishHairStyle() {
        super(0x164, true, true, true, false,
                0x09, 0x07, 0x60, 0x61, "Stylish");
    }

    @Override
    public void addHairInBack(AdvancedAppearance appearance) {
        super.addHairInBack(appearance);
        for (int y = 3; y <= 5; ++y) {
            FaceSpriteWithHair spr = new FaceSpriteWithHair(0x1ED + (y - 3) * 0x10, appearance.getHairColor());
            FaceSpriteWithHair spr2 = new FaceSpriteWithHair(0x1EE + (y - 3) * 0x10, appearance.getHairColor());
            if (y == 4) {
                appearance.setSprite(1, y, spr);
                appearance.setSprite(5, y, spr2);
            } else {
                appearance.addSpriteOnBelow(1, y, spr);
                appearance.addSpriteOnBelow(5, y, spr2);
            }
        }
    }
}
