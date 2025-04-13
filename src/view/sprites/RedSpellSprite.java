package view.sprites;

import view.MyColors;

public class RedSpellSprite extends SpellSprite {
    public RedSpellSprite(int col, boolean combat) {
        super(col, 2, combat, MyColors.BROWN, MyColors.DARK_RED, MyColors.WHITE);
    }
}
