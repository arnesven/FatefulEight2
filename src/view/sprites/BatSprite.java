package view.sprites;

import view.MyColors;

public class BatSprite extends LoopingSprite {
    public BatSprite() {
        super("batenemy", "enemies.png", 0x7A, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.LIGHT_GRAY);
        setColor3(MyColors.DARK_GRAY);
        setFrames(3);
    }
}
