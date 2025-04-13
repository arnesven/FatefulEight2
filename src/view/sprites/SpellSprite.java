package view.sprites;

import view.MyColors;

public class SpellSprite extends Sprite32x32 {
    private static final Sprite LITTLE_C = new Sprite32x32("romantwo", "items.png", 0x1E,
            MyColors.GRAY, MyColors.BLACK, MyColors.BLACK);

    public SpellSprite(int col, int row, boolean isCombatSpell, MyColors color2, MyColors color3, MyColors color4) {
        super("spell" + col + "x" + row, "spells.png", row * 0x10 + col,
                ItemSprite.FRAME_COLOR, color2, color3, color4);
        if (isCombatSpell) {
            addToOver(LITTLE_C);
        }
    }
}
