package view.combat;

import model.Model;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.Sprite32x32;

public class MansionTheme extends CombatTheme {

    private static final Sprite32x32 topSprite = new MansionSprite("walltop", 0x52);
    private static final Sprite32x32 bottomSprite = new MansionSprite("wallbottom", 0x53);
    private static final Sprite32x32 door = new MansionSprite("door", 0x54);
    private static final Sprite32x32 window = new MansionSprite("window", 0x55);
    private static final Sprite32x32 plant = new MansionSprite("plant", 0x56);
    private static final Sprite32x32 ground = new Sprite32x32("ground", "combat.png", 0x57,
            MyColors.DARK_BROWN, MyColors.CYAN, MyColors.BLACK, MyColors.CYAN);
    private static final Sprite32x32[] bottomRow = new Sprite32x32[]{
      plant, door, bottomSprite, window, bottomSprite, plant, bottomSprite, window
    };

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        for (int i = 0; i < 8; ++i) {
            model.getScreenHandler().put(xOffset+i*4, yOffset, topSprite);
        }
        for (int i = 0; i < bottomRow.length; ++i) {
            model.getScreenHandler().put(xOffset+i*4, yOffset+4, bottomRow[i]);
        }
        for (int i = 2; i < 9; ++i) {
            for (int j = 0; j < 8; ++j) {
                model.getScreenHandler().put(xOffset + j*4, yOffset + i*4, ground);
            }
        }
    }

    private static class MansionSprite extends Sprite32x32 {
        public MansionSprite(String name, int num) {
            super(name, "combat.png", num, MyColors.DARK_GRAY, MyColors.TAN, MyColors.GREEN, MyColors.CYAN);
        }
    }
}
