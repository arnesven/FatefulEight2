package view.sprites;

import view.MyColors;

public class HorseSprite extends Sprite {
    public HorseSprite(int col, int row, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super("horse", "horses.png", col, row, 64, 64);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }
}
