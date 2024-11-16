package model.items.imbuements;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class WeaponImbuement {
    private static final Sprite BURNING_SPRITE = CharSprite.make(0xDD, MyColors.LIGHT_GRAY, MyColors.RED, MyColors.BEIGE);

    public int[] makeDamageTable(int[] damageTable) {
        return damageTable;
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        screenHandler.put(col, row, BURNING_SPRITE);
    }
}
