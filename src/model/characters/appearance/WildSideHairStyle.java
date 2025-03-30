package model.characters.appearance;

import view.sprites.FaceSpriteWithHair;

public class WildSideHairStyle extends HairStyle5x2 {
    public WildSideHairStyle() {
        super(0x16A, false, true, 0x05, 0x12, 0x54, 0x55,"Wild Side");
    }

    @Override
    public void addHairInBack(AdvancedAppearance appearance) {
        for (int x = 1; x <= 2; ++x) {
            for (int y = 3; y <= 5; ++y) {
                FaceSpriteWithHair spr = new FaceSpriteWithHair(0x1D0 + 0x10 * y + x - 1, appearance.getHairColor());
                if (y == 4 && x == 1) {
                    appearance.setSprite(x, y, spr);
                } else {
                    appearance.addSpriteOnBelow(x, y, spr);
                }
            }
        }

        for (int x = 4; x <= 5; ++x) {
            for (int y = 3; y <= 5; ++y) {
                FaceSpriteWithHair spr = new FaceSpriteWithHair(0x1D0 + 0x10 * y + x - 2, appearance.getHairColor());
                if (y == 4 && x == 5) {
                    appearance.setSprite(x, y, spr);
                } else {
                    appearance.addSpriteOnBelow(x, y, spr);
                }
            }
        }
    }
}
