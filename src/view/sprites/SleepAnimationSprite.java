package view.sprites;

import view.MyColors;

public class SleepAnimationSprite extends LoopingSprite {
    public SleepAnimationSprite() {
        super("sleepanimation", "dungeon.png", 0x46, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.PINK);
        setFrames(2);
        setDelay(32);
    }
}
