package view.sprites;

import view.MyColors;

public class GoblinSprite extends LoopingSprite {
    public GoblinSprite(int num) {
        super("goblin"+num, "enemies.png", num, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.TAN);
        setColor3(MyColors.ORC_GREEN);
        setColor4(MyColors.GRAY);
        setFrames(4);
    }
}
