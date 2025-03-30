package model.characters.appearance;

import model.characters.FemaleLongHairStyle;
import view.sprites.FaceSpriteWithHair;
import view.sprites.HairSpriteWithFrame;

public class LongBangsHairStyle extends FemaleLongHairStyle {
    public LongBangsHairStyle() {
        super("Long Bangs");
    }

    @Override
    public void addHairInBack(AdvancedAppearance advancedAppearance) {
        super.addHairInBack(advancedAppearance);
        for (int y = 5; y <= 6; ++y) {
            for (int x = 1; x <= 2; ++x) {
                advancedAppearance.addSpriteOnTop(x, y, new HairSpriteWithFrame(0x1C3 + y*0x10 + x, advancedAppearance.getHairColor()));
            }
            for (int x = 4; x <= 5; ++x) {
                advancedAppearance.addSpriteOnTop(x, y, new HairSpriteWithFrame(0x1C2 + y*0x10 + x, advancedAppearance.getHairColor()));
            }
        }
    }
}
