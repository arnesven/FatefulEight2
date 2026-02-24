package model.characters.appearance;

import view.sprites.HairSpriteWithFrame;

public class WavyLongHairStyle extends HairStyle5x2 {
    public WavyLongHairStyle() {
        super(0x260, true, true,
                0x01, 0x02, 0x54, 0x55, "Wavy/Long");
    }


    @Override
    public void addHairInBack(AdvancedAppearance advancedAppearance) {
        super.addHairInBack(advancedAppearance);
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 2; ++x) {
                advancedAppearance.addSpriteOnTop(x+1, y+3,
                        new HairSpriteWithFrame(0x255 + y*0x10 + x, advancedAppearance.getHairColor()));
            }
            advancedAppearance.addSpriteOnTop(5, y+3,
                    new HairSpriteWithFrame(0x257 + y*0x10, advancedAppearance.getHairColor()));
        }
    }
}
