package view.sprites;

import view.MyColors;

public class ColorlessSpellSprite extends SpellSprite {
    public ColorlessSpellSprite(int col, boolean combat) {
        super(col, 5, combat, MyColors.BROWN, MyColors.PEACH, MyColors.BLACK);
    }
}
