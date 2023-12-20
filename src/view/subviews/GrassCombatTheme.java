package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;

public class GrassCombatTheme extends CombatTheme {
    private Random random;
    private static Sprite treeUpperSprite = new Sprite32x32("treeupper", "combat.png", 0x10, MyColors.DARK_GREEN, MyColors.GREEN, MyColors.CYAN);
    private static Sprite treeLowerSprite = new Sprite32x32("treelower", "combat.png", 0x11, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.DARK_GRAY);
    private static Sprite grassLineSprite = new Sprite32x32("treelower", "combat.png", 0x12, MyColors.GREEN, MyColors.DARK_GREEN, MyColors.GREEN);
    private static Sprite upperContour = new Sprite32x32("treecontourupper", "combat.png", 0x20, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.WHITE);
    private static Sprite lowerContour = new Sprite32x32("treecontourlower", "combat.png", 0x21, MyColors.BLACK, MyColors.RED, MyColors.RED);
    public static Sprite[] grassSprites = makeGroundSprites(MyColors.GREEN, MyColors.LIGHT_GREEN, GRASSY_ROW);
    public static Sprite[] darkGrassSprites = makeGroundSprites(MyColors.DARK_GREEN, MyColors.GREEN, GRASSY_ROW);


    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        random = new Random(555);
        for (int i = 0; i < 8; ++i) {
            model.getScreenHandler().put(xOffset + i*4, yOffset, treeUpperSprite);
            model.getScreenHandler().register(upperContour.getName() + i, new Point(xOffset + i*4, yOffset), upperContour);
            model.getScreenHandler().put(xOffset  + i*4, yOffset + 4, treeLowerSprite);
            model.getScreenHandler().register(lowerContour.getName() + i, new Point(xOffset + i*4, yOffset+4), lowerContour);
            model.getScreenHandler().put(xOffset  + i*4, yOffset + 8, grassLineSprite);
            for (int y= 0; y < 6; y++) {
                model.getScreenHandler().put(xOffset + i*4, yOffset + (y+3)*4, grassSprites[random.nextInt(grassSprites.length)]);
            }
        }
    }
}
