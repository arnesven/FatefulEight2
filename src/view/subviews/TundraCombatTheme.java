package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;

import java.util.Random;

public class TundraCombatTheme extends MountainCombatTheme {
    private Random random;
    public static Sprite[] groundSprites = makeGroundSprites(MyColors.WHITE, MyColors.GRAY, ROCKY_ROW);

    @Override
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
