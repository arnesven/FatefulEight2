package view.sprites;

import view.MyColors;

public class MovingRightArrow extends AnimatedCharSprite {
    public MovingRightArrow(MyColors fgColor, MyColors bgColor) {
        super((char)0x10, MyColors.WHITE, fgColor, bgColor, 2);
    }
}
