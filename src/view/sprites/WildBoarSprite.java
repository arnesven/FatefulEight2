package view.sprites;

import view.MyColors;

public class WildBoarSprite extends LoopingSprite {
    public WildBoarSprite(String wildboar, String s, int i) {
        super(wildboar, s, i, 64, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.TAN);
        setColor3(MyColors.BROWN);
        setFrames(6);
        setDelay(8);
    }
}
