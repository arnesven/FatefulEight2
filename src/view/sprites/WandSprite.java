package view.sprites;

import view.MyColors;

public class WandSprite extends Sprite {
    public WandSprite(MyColors handColor, MyColors magicColor) {
        super("duelistwandsprite", "gauge.png", 2, 2, 64, 64);
        setColor1(MyColors.DARK_GRAY);
        setColor2(handColor);
        setColor3(MyColors.BROWN);
        setColor4(magicColor);
    }
}
