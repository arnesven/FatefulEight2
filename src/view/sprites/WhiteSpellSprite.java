package view.sprites;

import view.MyColors;

public class WhiteSpellSprite extends SpellSprite {
    public WhiteSpellSprite(int col, boolean isCombat) {
        super(col, 1, isCombat, MyColors.BROWN, MyColors.WHITE, MyColors.DARK_GRAY);
    }
}
