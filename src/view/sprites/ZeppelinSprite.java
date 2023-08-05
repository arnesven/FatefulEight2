package view.sprites;

import view.MyColors;

public class ZeppelinSprite extends LoopingSprite {
    public ZeppelinSprite() {
        super("zeppelinani", "world.png", 0x54, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.DARK_PURPLE);
        setColor3(MyColors.DARK_GRAY);
        setColor4(MyColors.BROWN);
        setFrames(4);
        setDelay(32);
    }
}
