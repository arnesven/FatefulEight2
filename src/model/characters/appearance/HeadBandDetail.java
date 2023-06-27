package model.characters.appearance;

import model.races.Race;
import view.sprites.Sprite8x8;

public class HeadBandDetail extends FaceDetail {
    public HeadBandDetail() {
        super("Bandana");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        for (int i = 0; i < 3; ++i) {
            addSpriteOnTop(appearance, 0x7A + i, 2+i, 2);
        }
        addSpriteOnTop(appearance, 0x8C, 4, 3);
        addSpriteOnTop(appearance, 0x9C, 4, 4);
        addSpriteOnTop(appearance, 0x9D, 5, 4);
    }

    private void addSpriteOnTop(AdvancedAppearance appearance, int num, int x, int y) {
        Sprite8x8 left = new Sprite8x8("headband", "clothes.png", num);
        left.setColor1(getColor());
        appearance.addSpriteOnTop(x, y, left);
    }
}
