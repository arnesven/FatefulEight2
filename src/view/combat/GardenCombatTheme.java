package view.combat;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;

public class GardenCombatTheme extends CombatTheme {
    private static final Sprite treeUpperSprite = new Sprite32x32("gardenupper", "combat.png", 0x10, MyColors.DARK_GREEN, MyColors.DARK_GRAY, MyColors.BLACK);
    private static final Sprite treeLowerSprite = new Sprite32x32("gardenlower", "combat.png", 0x11, MyColors.TAN, MyColors.DARK_GRAY, MyColors.BLACK);
    private static final Sprite grassLineSprite = new Sprite32x32("gardenlower", "combat.png", 0x12, MyColors.DARK_GREEN, MyColors.DARK_GRAY, MyColors.GREEN);
    private static final Sprite upperContour = new Sprite32x32("gardencontourupper", "combat.png", 0x20, MyColors.ORC_GREEN, MyColors.BLACK, MyColors.BLACK);
    private static final Sprite lowerContour = new Sprite32x32("gardencontourlower", "combat.png", 0x21, MyColors.DARK_BROWN, MyColors.RED, MyColors.RED);
    public static Sprite[] darkGrassSprites = makeGroundSprites(MyColors.DARK_GREEN, MyColors.GREEN, GRASSY_ROW);

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        Random random = new Random(555);
        for (int i = 0; i < 8; ++i) {
            model.getScreenHandler().put(xOffset + i*4, yOffset, treeUpperSprite);
            model.getScreenHandler().register(upperContour.getName() + i, new Point(xOffset + i*4, yOffset), upperContour);
            model.getScreenHandler().put(xOffset  + i*4, yOffset + 4, treeLowerSprite);
            model.getScreenHandler().register(lowerContour.getName() + i, new Point(xOffset + i*4, yOffset+4), lowerContour);
            model.getScreenHandler().put(xOffset  + i*4, yOffset + 8, grassLineSprite);
            for (int y= 0; y < 6; y++) {
                model.getScreenHandler().put(xOffset + i*4, yOffset + (y+3)*4, darkGrassSprites[random.nextInt(darkGrassSprites.length)]);
            }
        }
    }
}
