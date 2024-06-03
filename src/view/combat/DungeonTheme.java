package view.combat;

import model.Model;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.Sprite32x32;

public class DungeonTheme extends CombatTheme {

    private static final Sprite32x32 topSprite = new DungeonSprite("walltop", 0x41);
    private static final Sprite32x32 bottomSprite = new DungeonSprite("wallbottom", 0x42);
    private static final Sprite32x32 archUL = new DungeonSprite("archUL", 0x43);
    private static final Sprite32x32 archUR = new DungeonSprite("archUR", 0x44);
    private static final Sprite32x32 wallLeft = new DungeonSprite("wallleft", 0x45);
    private static final Sprite32x32 wallRight = new DungeonSprite("allright", 0x46);
    private static final Sprite32x32[] topRow = new Sprite32x32[] {
            archUL, archUR, topSprite, topSprite, topSprite, topSprite, archUL, archUR};
    private static final Sprite32x32[] bottomRow = new Sprite32x32[] {
            wallLeft, wallRight, bottomSprite, bottomSprite, bottomSprite, bottomSprite, wallLeft, wallRight};
    private static final Sprite32x32 ground = new DungeonSprite("ground", 0x47);

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        for (int i = 0; i < topRow.length; ++i) {
            model.getScreenHandler().put(xOffset + i*4, yOffset, topRow[i]);
            model.getScreenHandler().put(xOffset + i*4, yOffset + 4, bottomRow[i]);
        }
        for (int i = 2; i < 9; ++i) {
            for (int j = 0; j < 8; ++j) {
                model.getScreenHandler().put(xOffset + j*4, yOffset + i*4, ground);
            }
        }
    }

    private static class DungeonSprite extends Sprite32x32 {
        public DungeonSprite(String name, int num) {
            super(name, "combat.png", num, MyColors.DARK_GRAY, MyColors.DARK_RED, MyColors.TAN, MyColors.YELLOW);
        }
    }
}
