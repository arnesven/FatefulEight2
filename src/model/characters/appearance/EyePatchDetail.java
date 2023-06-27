package model.characters.appearance;

import model.races.Race;
import view.sprites.Sprite8x8;

public class EyePatchDetail extends FaceDetail {
    public EyePatchDetail() {
        super("Patch");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        for (int i = 0; i < 3; ++i) {
            Sprite8x8 left = new Sprite8x8("eyepatch", "clothes.png", 0x5A + i);
            left.setColor1(getColor());
            appearance.addSpriteOnTop(2 + i, 3, left);
        }
    }
}
