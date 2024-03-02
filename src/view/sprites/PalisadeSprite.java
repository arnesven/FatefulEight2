package view.sprites;

import view.MyColors;

public class PalisadeSprite extends Sprite32x32{
    public PalisadeSprite(int num) {
        super("palisade" + num, "quest.png", num,
                MyColors.TAN, MyColors.DARK_BROWN, MyColors.BEIGE, MyColors.GRAY);
    }
}
