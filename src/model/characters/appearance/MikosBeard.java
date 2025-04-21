package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.FaceAndClothesSprite;
import view.sprites.FaceSprite;

public class MikosBeard extends NoBeard {
    private final MyColors lineColor;

    public MikosBeard(MyColors color) {
        this.lineColor = color;
    }

    @Override
    public boolean isTrueBeard() {
        return true;
    }
    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        FaceSprite spr = new FaceAndClothesSprite(0x155, appearance.getFacialHairColor());
        spr.setColor1(lineColor);
        appearance.addSpriteOnTop(3, 5, spr);
    }
}
