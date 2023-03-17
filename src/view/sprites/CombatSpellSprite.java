package view.sprites;

import view.MyColors;

public class CombatSpellSprite extends OverlayItemSprite {
    private static final Sprite LITTLE_C = new Sprite32x32("romantwo", "items.png", 0x1E,
            MyColors.BLACK, MyColors.BLACK, MyColors.BLACK);

    public CombatSpellSprite(int col, int row, MyColors color1, MyColors color2, MyColors color3) {
        super(col, row, color1, color2, color3, LITTLE_C);
    }
}
