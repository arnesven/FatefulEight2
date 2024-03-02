package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class PalisadeCombatTheme extends MountainCombatTheme {

    private final boolean inside;

    public PalisadeCombatTheme(boolean inside) {
        this.inside = inside;
    }

    private static final Sprite PALISADE_UPPER = new Sprite32x32("palisdaecombatupper", "combat.png", 0x9A,
            MyColors.TAN, MyColors.BROWN, MyColors.BEIGE, MyColors.GRAY);
    private static final Sprite PALISADE_LOWER = new Sprite32x32("palisadecombatlower", "combat.png", 0x99,
            MyColors.TAN, MyColors.BROWN, MyColors.BEIGE, MyColors.GRAY);
    private static final Sprite PALISADE_STAIRS = new Sprite32x32("palisadestairs", "combat.png", 0x98,
            MyColors.TAN, MyColors.BROWN, MyColors.BEIGE, MyColors.GRAY);

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        super.drawBackground(model, xOffset, yOffset);
        drawGround(model, xOffset, yOffset-8);

        int yShift = inside ? 0 : 6;

        for (int x = 0; x < 8; ++x) {
            model.getScreenHandler().put(xOffset + 4 * x, yOffset + 4 * yShift, PALISADE_UPPER);
            model.getScreenHandler().put(xOffset + 4 * x, yOffset + 4 * (yShift + 1), PALISADE_LOWER);
        }
        model.getScreenHandler().put(xOffset + 4 * 2, yOffset + 4 * (yShift + 1), PALISADE_STAIRS);
        model.getScreenHandler().put(xOffset + 4 * 7, yOffset + 4 * (yShift + 1), PALISADE_STAIRS);
    }
}
