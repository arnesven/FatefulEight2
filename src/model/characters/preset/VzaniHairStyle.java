package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.ExplicitHairStyle;
import view.sprites.FaceSpriteWithHair;

public class VzaniHairStyle extends ExplicitHairStyle {
    public VzaniHairStyle() {
        super(true, 0x04, 0xEF, 0x14, 0x05, 0xFC, 0x15, 0x30, 0x31, "Trendy");
    }

    @Override
    public void apply(AdvancedAppearance appearance) {
        appearance.setSprite(1, 4, new FaceSpriteWithHair(0xF0, appearance.getHairColor()));
        appearance.setSprite(5, 4, new FaceSpriteWithHair(0xF1, appearance.getHairColor()));
    }
}
