package view.sprites;

import view.MyColors;

public class BoatSprite extends Sprite32x32{
    public BoatSprite(int num) {
        super("boatsprite"+num, "world_foreground.png", num, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.GRAY_RED);
    }
}
