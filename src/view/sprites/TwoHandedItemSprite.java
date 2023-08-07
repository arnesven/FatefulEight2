package view.sprites;

import view.MyColors;

public class TwoHandedItemSprite extends OverlayItemSprite {
    private static final Sprite ROMAN_TWO = new Sprite32x32("romantwo", "items.png", 0x0E,
            MyColors.GRAY, MyColors.BLACK, MyColors.BLACK);

    public TwoHandedItemSprite(int col, int row, MyColors color2, MyColors color3, MyColors color4) {
        super(col, row, color2, color3, color4, ROMAN_TWO);
        addToOver(ROMAN_TWO);
    }

    public TwoHandedItemSprite(int col, int row) {
        super(col, row, ROMAN_TWO);
        addToOver(ROMAN_TWO);
    }
}
