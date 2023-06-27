package model.characters.appearance;

import model.races.Race;
import view.sprites.Sprite8x8;

public class EarringsDetail extends FaceDetail {
    public EarringsDetail() {
        super("Earrings");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        if (!coversEars) {
            Sprite8x8 left = new Sprite8x8("earringleft", "clothes.png", 0x4A);
            left.setColor1(getColor());
            appearance.addSpriteOnTop(1, 3, left);
            Sprite8x8 right = new Sprite8x8("earringright", "clothes.png", 0x4B);
            right.setColor1(getColor());
            appearance.addSpriteOnTop(5, 3, right);
        }
    }
}
