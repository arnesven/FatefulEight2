package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.FaceAndClothesSprite;
import view.sprites.FaceSprite;
import view.sprites.FilledBlockSprite;

public class LongBeard extends Beard {
    private final MyColors lineColor;

    public LongBeard(MyColors lineColor) {
        super(0);
        this.lineColor = lineColor;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        for (int y = 4; y <= 6; ++y) {
            FaceSprite spr = new FaceAndClothesSprite(0x3A + 0x10*y, appearance.getFacialHairColor());
            spr.setColor1(lineColor);
            appearance.addSpriteOnTop(2, y, spr);
            FaceSprite spr2 = new FaceAndClothesSprite(0x3B + 0x10*y, appearance.getFacialHairColor());
            spr2.setColor1(lineColor);
            appearance.addSpriteOnTop(4, y, spr2);
            if (y > 4) {
                appearance.setSprite(3, y, new FilledBlockSprite(appearance.getFacialHairColor()));
            }
        }
        appearance.setSprite(3, 6, new FaceAndClothesSprite(0xFA, appearance.getFacialHairColor()));

    }
}
