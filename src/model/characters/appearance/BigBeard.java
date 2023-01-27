package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.FaceSprite;

public class BigBeard extends Beard {
    private final MyColors lineColor;

    public BigBeard(MyColors lineColor) {
        super(new int[]{0xFB, 0xFB}, 0x44);
        this.lineColor = lineColor;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        for (int y = 4; y <= 5; ++y) {
            FaceSprite spr = new FaceSprite(0x10 * y + 0x35);
            spr.setColor2(appearance.getFacialHairColor());
            spr.setColor1(lineColor);
            appearance.addSpriteOnTop(1, y, spr);
            FaceSprite spr2 = new FaceSprite(0x10 * y + 0x36);
            spr2.setColor2(appearance.getFacialHairColor());
            spr2.setColor1(lineColor);
            appearance.addSpriteOnTop(5, y, spr2);
        }

        for (int x = 1; x <= 5; ++x) {
            FaceSprite spr = new FaceSprite(0x94 + x);
            spr.setColor2(appearance.getFacialHairColor());
            spr.setColor1(lineColor);
            appearance.addSpriteOnTop(x, 6, spr);
        }

        for (int x = 2; x <= 4; ++x) {
            FaceSprite spr = new FaceSprite(0xFB);
            spr.setColor2(appearance.getFacialHairColor());
            spr.setColor1(lineColor);
            appearance.addSpriteOnTop(x, 5, spr);
        }
    }

    protected MyColors getLineColor() {
        return lineColor;
    }
}
