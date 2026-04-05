package view.sprites;

import view.MyColors;

public class MineRockSprite extends Sprite32x32 {
    public MineRockSprite(int num, MyColors color1, MyColors color2) {
        super("justarock"+num, "warehouse.png",
                num, color1, color2, color2, color2);
    }
}
