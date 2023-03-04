package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.Random;

public class MountainCombatTheme extends CombatTheme {
    private Random random;
    private static Sprite mountUL = new Sprite32x32("mountul", "combat.png", 0x70, MyColors.GRAY, MyColors.TAN, MyColors.WHITE, MyColors.CYAN);
    private static Sprite mountUR = new Sprite32x32("mountur", "combat.png", 0x71, MyColors.GRAY, MyColors.TAN, MyColors.WHITE, MyColors.CYAN);
    private static Sprite mountLL = new Sprite32x32("mountll", "combat.png", 0x80, MyColors.GRAY, MyColors.TAN, MyColors.WHITE, MyColors.CYAN);
    private static Sprite mountLR = new Sprite32x32("mountlr", "combat.png", 0x81, MyColors.GRAY, MyColors.TAN, MyColors.WHITE, MyColors.CYAN);
    public static Sprite[] groundSprites = makeGroundSprites(MyColors.TAN, MyColors.GRAY, ROCKY_ROW);

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        for (int i = 0; i < 4; ++i) {
            model.getScreenHandler().put(xOffset + i*8, yOffset, mountUL);
            model.getScreenHandler().put(xOffset + i*8+4, yOffset, mountUR);
            model.getScreenHandler().put(xOffset + i*8, yOffset+4, mountLL);
            model.getScreenHandler().put(xOffset + i*8+4, yOffset+4, mountLR);
        }
        drawGround(model, xOffset, yOffset);
    }

    protected void drawGround(Model model, int xOffset, int yOffset) {
        random = new Random(555);
        for (int i = 0; i < 8; ++i) {
            for (int y= 0; y < 7; y++) {
                model.getScreenHandler().put(xOffset + i*4, yOffset + (y+2)*4,
                        groundSprites[random.nextInt(groundSprites.length)]);
            }
        }
    }
}
