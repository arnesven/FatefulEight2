package view.subviews;

import model.Model;
import model.map.CaveHex;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;

import java.util.Random;

public class CaveTheme extends CombatTheme {

    private static final Sprite BG_UL = new CaveSprite("cavebgul", 0xE0);
    private static final Sprite BG_UR = new CaveSprite("cavebgur", 0xE1);
    private static final Sprite BG_LL = new CaveSprite("cavebgll", 0xF0);
    private static final Sprite BG_LR = new CaveSprite("cavebglr", 0xF1);

    private static final Sprite[] groundSprites = new Sprite32x32[] {
            new CaveGroundSprite(0xF2),
            new CaveGroundSprite(0xE2), new CaveGroundSprite(0xE3), new CaveGroundSprite(0xE4),
            new CaveGroundSprite(0xE5), new CaveGroundSprite(0xE6), new CaveGroundSprite(0xE7),
    };
    private static final int STALAGMITE_SPARSITY = 3;

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        for (int i = 0; i < 8; i += 2) {
            model.getScreenHandler().put(xOffset + i*4, yOffset, BG_UL);
            model.getScreenHandler().put(xOffset + (i+1)*4, yOffset, BG_UR);
            model.getScreenHandler().put(xOffset + i*4, yOffset+4, BG_LL);
            model.getScreenHandler().put(xOffset + (i+1)*4, yOffset+4, BG_LR);
        }
        Random random = new Random(431);
        for (int i = 2; i < 9; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (random.nextInt(STALAGMITE_SPARSITY) == 0) {
                    model.getScreenHandler().put(xOffset + j * 4, yOffset + i * 4,
                            groundSprites[random.nextInt(6)]);
                } else {
                    model.getScreenHandler().put(xOffset + j * 4, yOffset + i * 4,
                            groundSprites[0]);
                }
            }
        }
    }

    private static class CaveSprite extends Sprite32x32 {
        public CaveSprite(String name, int num) {
            super(name, "combat.png", num, CaveHex.GROUND_COLOR, MyColors.DARK_BROWN, MyColors.DARK_GRAY, MyColors.GRAY);
        }
    }

    private static class CaveGroundSprite extends Sprite32x32 {
        public CaveGroundSprite(int i) {
            super("caveground" + i, "combat.png", i, CaveHex.GROUND_COLOR, MyColors.DARK_GRAY, CaveHex.GROUND_COLOR, MyColors.YELLOW);
        }
    }
}
