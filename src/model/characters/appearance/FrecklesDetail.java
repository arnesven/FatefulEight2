package model.characters.appearance;

import model.races.Race;
import view.sprites.Sprite8x8;

public class FrecklesDetail extends FaceDetail {
    public FrecklesDetail() {
        super("Freckles");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        Sprite8x8 frecks = new Sprite8x8("freckles", "clothes.png", 0x4C);
        frecks.setColor1(getColor());
        appearance.addSpriteOnTop(3, 3, frecks);
    }
}
