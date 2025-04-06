package view.combat;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.Random;

public class IslandCombatTheme extends CombatTheme {
    private static final MyColors TREE_COLOR = MyColors.DARK_GREEN;
    private static final MyColors SAND_COLOR = MyColors.LIGHT_YELLOW;
    private static final MyColors WATER_COLOR = MyColors.CYAN;
    private static final MyColors SKY_COLOR = MyColors.LIGHT_BLUE;
    private final Sprite[] beachSprites;
    private final Sprite[] grassSprites;
    private final Sprite32x32 seascapeUL;
    private final Sprite32x32 seascapeUR;
    private final Sprite32x32 seascapeLL;
    private final Sprite32x32 seascapeLR;
    private Random random;

    public IslandCombatTheme() {
        seascapeUL = new Sprite32x32("seascapeul", "combat.png", 0xAE, TREE_COLOR, SAND_COLOR, WATER_COLOR, SKY_COLOR);
        seascapeUR = new Sprite32x32("seascapeur", "combat.png", 0xAF, TREE_COLOR, SAND_COLOR, WATER_COLOR, SKY_COLOR);
        seascapeLL = new Sprite32x32("seascapell", "combat.png", 0xBE, TREE_COLOR, SAND_COLOR, WATER_COLOR, SKY_COLOR);
        seascapeLR = new Sprite32x32("seascapelr", "combat.png", 0xBF, TREE_COLOR, SAND_COLOR, WATER_COLOR, SKY_COLOR);

        this.beachSprites = makeGroundSprites(SAND_COLOR, MyColors.GRAY, ROCKY_ROW);
        this.grassSprites = makeGroundSprites(MyColors.GREEN, MyColors.LIGHT_GREEN, GRASSY_ROW);
    }

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        for (int i = 0; i < 4; ++i) {
            model.getScreenHandler().put(xOffset + i*8, yOffset, seascapeUL);
            model.getScreenHandler().put(xOffset + i*8+4, yOffset, seascapeUR);
            model.getScreenHandler().put(xOffset + i*8, yOffset+4, seascapeLL);
            model.getScreenHandler().put(xOffset + i*8+4, yOffset+4, seascapeLR);
        }
        random = new Random(123);
        for (int i = 0; i < 8; ++i) {
            for (int y = 0; y < 7; y++) {
                Sprite spriteToUse;
                if (y == 0) {
                    spriteToUse = beachSprites[random.nextInt(beachSprites.length)];
                } else {
                    spriteToUse = grassSprites[random.nextInt(grassSprites.length)];
                }
                model.getScreenHandler().put(xOffset + i * 4, yOffset + (y + 2) * 4,
                        spriteToUse);
            }
        }
    }
}
