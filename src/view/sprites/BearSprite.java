package view.sprites;

import view.MyColors;

public class BearSprite extends LoopingSprite {
    public BearSprite(String name, String path, int num) {
        super(name, path, num, 64, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.TAN);
        setColor3(MyColors.BROWN);
        setFrames(4);
    }
}
