package view.combat;

import model.Model;
import view.MyColors;
import view.sprites.Sprite32x32;
import view.combat.CombatTheme;

import java.util.Random;

public class TownCombatTheme extends CombatTheme {

    private static final Sprite32x32 topYellow = new TownSprite("topYellow", 0x62, MyColors.LIGHT_YELLOW);
    private static final Sprite32x32 bottomYellow = new TownSprite("bottomYellow", 0x63, MyColors.LIGHT_YELLOW);
    private static final Sprite32x32 topPink = new TownSprite("topPink", 0x62, MyColors.PINK);
    private static final Sprite32x32 bottomPink = new TownSprite("bottomPink", 0x63, MyColors.PINK);
    private static final Sprite32x32 topWhite = new TownSprite("topWhite", 0x62, MyColors.WHITE);
    private static final Sprite32x32 bottomWhite = new TownSprite("bottomWhite", 0x63, MyColors.WHITE);

    public static final Sprite32x32[] topRow = new Sprite32x32[] {
            topYellow, topPink, topWhite, topPink, topYellow, topYellow, topPink, topWhite};
    public static final Sprite32x32[] bottomRow = new Sprite32x32[] {
            bottomYellow, bottomPink, bottomWhite, bottomPink,
            bottomYellow, bottomYellow, bottomPink, bottomWhite};
    public static final Sprite32x32 ground = new Sprite32x32("ground", "combat.png", 0x64,
            MyColors.GRAY, MyColors.DARK_GRAY, MyColors.TAN, MyColors.CYAN);
    public static final Sprite32x32 ground2 = new Sprite32x32("ground2", "combat.png", 0x65,
            MyColors.GRAY, MyColors.DARK_GRAY, MyColors.TAN, MyColors.CYAN);
    public static final Sprite32x32 ground3 = new Sprite32x32("ground3", "combat.png", 0x66,
            MyColors.GRAY, MyColors.DARK_GRAY, MyColors.TAN, MyColors.CYAN);
    public static final Sprite32x32 ground4 = new Sprite32x32("ground4", "combat.png", 0x67,
            MyColors.GRAY, MyColors.DARK_GRAY, MyColors.TAN, MyColors.CYAN);

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        for (int i = 0; i < topRow.length; ++i) {
            model.getScreenHandler().put(xOffset + i*4, yOffset, topRow[i]);
            model.getScreenHandler().put(xOffset + i*4, yOffset + 4, bottomRow[i]);
        }
        Random random = new Random(123);
        for (int i = 2; i < 9; ++i) {
            for (int j = 0; j < 8; ++j) {
                int rnd = random.nextInt(16);
                if (i+j == rnd) {
                    model.getScreenHandler().put(xOffset + j * 4, yOffset + i * 4, ground2);
                } else if (i+j+1 == rnd) {
                    model.getScreenHandler().put(xOffset + j * 4, yOffset + i * 4, ground3);
                } else if (i+j+2 == rnd) {
                    model.getScreenHandler().put(xOffset + j * 4, yOffset + i * 4, ground4);
                } else {
                    model.getScreenHandler().put(xOffset + j * 4, yOffset + i * 4, ground);
                }
            }
        }
    }

    private static class TownSprite extends Sprite32x32 {
        public TownSprite(String name, int num, MyColors houseColor) {
            super(name, "combat.png", num, houseColor, MyColors.BLACK, MyColors.GRAY, MyColors.CYAN);
        }
    }
}
