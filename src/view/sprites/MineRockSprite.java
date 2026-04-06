package view.sprites;

import view.MyColors;

public class MineRockSprite extends Sprite32x32 {
    public MineRockSprite(int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super("justarock"+num, "warehouse.png",
                num, color1, color2, color3, color4);
    }

    public MineRockSprite(int num, MyColors color1, MyColors color2) {
        this(num, color1, color2, color2, color2);
    }
}
