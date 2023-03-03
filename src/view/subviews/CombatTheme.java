package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public abstract class CombatTheme {
    public static final int ROCKY_ROW = 0;
    public static final int GRASSY_ROW = 1;

    public abstract void drawBackground(Model model, int xOffset, int yOffset);

    protected static Sprite[] makeGroundSprites(MyColors color1, MyColors color2, int row) {
        Sprite[] result = new Sprite[5];
        for (int i = 0; i < 5; ++i) {
            result[i] = new Sprite32x32("grass"+i, "combat.png", 0x10*row+i+3, color1, color2, MyColors.BEIGE);
        }
        return result;
    }
}
