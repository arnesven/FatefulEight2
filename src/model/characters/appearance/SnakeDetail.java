package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.Sprite8x8;

public class SnakeDetail extends FaceDetail {
    public SnakeDetail() {
        super("Snake");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        for (int y = 5; y < 7; ++y) {
            for (int i = 0; i < 7; ++i) {
                addSpriteOnTop(appearance, 0x168 + 0x10*y + i, i, y);
            }
        }
    }

    private void addSpriteOnTop(AdvancedAppearance appearance, int num, int x, int y) {
        Sprite8x8 left = new Sprite8x8("headband", "clothes.png", num);
        left.setColor2(getColor());
        appearance.addSpriteOnTop(x, y, left);
    }
}
