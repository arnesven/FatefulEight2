package view.sprites;

import view.MyColors;

public class GoblinSprite extends LoopingSprite {
    public GoblinSprite(int num, MyColors color4) {
        super("goblin"+num, "enemies.png", num, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.TAN);
        setColor3(MyColors.ORC_GREEN);
        setColor4(color4);
        setFrames(4);
    }

    public GoblinSprite(int num) {
        this(num, MyColors.GRAY);
    }
}
