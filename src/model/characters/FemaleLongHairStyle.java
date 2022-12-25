package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.HairStyle3x2;
import view.sprites.FaceSpriteWithHair;

public class FemaleLongHairStyle extends HairStyle3x2 {
    public FemaleLongHairStyle() {
        super(0x7D, true, true, true, true);
    }

    public FemaleLongHairStyle(int num) {
        super(num, true, true, true, true);
    }

    @Override
    public void apply(AdvancedAppearance appearance) {
        appearance.setSprite(1, 4, new FaceSpriteWithHair(0xF0, appearance.getHairColor()));
        appearance.setSprite(5, 4, new FaceSpriteWithHair(0xF1, appearance.getHairColor()));
    }

    @Override
    public void addHairInBack(AdvancedAppearance advancedAppearance) {
        super.addHairInBack(advancedAppearance);
        advancedAppearance.addSpriteOnBelow(1, 5, new FaceSpriteWithHair(0x93, advancedAppearance.getHairColor()));
        advancedAppearance.addSpriteOnBelow(5, 5, new FaceSpriteWithHair(0x94, advancedAppearance.getHairColor()));
    }
}
