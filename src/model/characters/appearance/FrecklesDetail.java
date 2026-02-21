package model.characters.appearance;

import model.races.Race;
import view.sprites.Sprite8x8;

public class FrecklesDetail extends FaceDetail {
    public FrecklesDetail() {
        super("Freckles");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        for (int i = 0; i < 3; ++i) {
            Sprite8x8 frecks = new Sprite8x8("freckles", "clothes.png", 0x1A8 + i);
            frecks.setColor1(getColor());
            appearance.addSpriteOnTop(2+i, 3, frecks);
        }
    }
}
