package view.sprites;

import view.MyColors;

public class FrogManSprite extends LoopingSprite {
    public FrogManSprite(int num) {
        super("frogman" + num, "enemies.png", num, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.DARK_GREEN);
        setColor3(MyColors.BROWN);
        setColor4(MyColors.LIGHT_YELLOW);
        setFrames(4);
    }
}
