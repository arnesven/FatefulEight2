package model.characters.appearance;

import model.races.Race;
import view.sprites.Sprite8x8;

public class GlassesDetail extends FaceDetail {
    public GlassesDetail() {
        super("Glasses");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
            for (int i = 0; i < 3; ++i) {
                Sprite8x8 left = new Sprite8x8("glasses", "clothes.png", 0x3A + i);
                left.setColor1(getColor());
                appearance.addSpriteOnTop(2 + i, 3, left);
            }
    }
}
