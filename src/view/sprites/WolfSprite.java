package view.sprites;

import view.MyColors;

public class WolfSprite extends LoopingSprite {
    public WolfSprite(String wolf, String s, int i) {
        super(wolf, s, i, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.GRAY);
        setColor3(MyColors.DARK_GRAY);
        setFrames(6);
        setDelay(8);
    }
}
