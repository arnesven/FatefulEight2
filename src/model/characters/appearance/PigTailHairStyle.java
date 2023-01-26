package model.characters.appearance;

import view.sprites.FaceSpriteWithHair;

public class PigTailHairStyle extends HairStyle3x2 {
    public PigTailHairStyle(int num, boolean inForehead, int normalAvatar) {
        super(num, inForehead, true, false, false, normalAvatar, 0x36);
    }

    @Override
    public void addHairInBack(AdvancedAppearance advancedAppearance) {
        advancedAppearance.addSpriteOnBelow(2, 4, new FaceSpriteWithHair(0x7C, advancedAppearance.getHairColor()));
        advancedAppearance.addSpriteOnBelow(2, 5, new FaceSpriteWithHair(0x8C, advancedAppearance.getHairColor()));
        advancedAppearance.addSpriteOnBelow(4, 4, new FaceSpriteWithHair(0x9C, advancedAppearance.getHairColor()));
        advancedAppearance.addSpriteOnBelow(4, 5, new FaceSpriteWithHair(0xAC, advancedAppearance.getHairColor()));
    }
}
