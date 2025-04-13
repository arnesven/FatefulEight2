package view.sprites;

import view.MyColors;

public class BlackSpellSprite extends SpellSprite {
    public BlackSpellSprite(int col, boolean combat) {
        super(col, 4,  combat, MyColors.BROWN, MyColors.GRAY, MyColors.RED);
    }
}
