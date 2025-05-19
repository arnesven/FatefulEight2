package model.characters.appearance;

import model.races.Race;
import view.sprites.Sprite8x8;

public class RougeDetail extends FaceDetail {
    public RougeDetail() {
        super("Rouge");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        Sprite8x8 left = new Sprite8x8("roguespotleft", "clothes.png", 0x16A);
        left.setColor1(getColor());
        appearance.addSpriteOnTop(2, 3, left);
        Sprite8x8 right = new Sprite8x8("roguespotright", "clothes.png", 0x17A);
        right.setColor1(getColor());
        appearance.addSpriteOnTop(4, 3, right);
    }
}
