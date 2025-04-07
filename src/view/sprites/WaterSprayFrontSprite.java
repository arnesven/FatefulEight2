package view.sprites;

import view.MyColors;

public class WaterSprayFrontSprite extends LoopingSprite {
    public WaterSprayFrontSprite() {
        super("watersorayfront", "quest.png", 0xC2, 32);
        setFrames(3);
        setColor1(MyColors.CYAN);
        setColor2(MyColors.WHITE);
        shiftUpPx(-2);
    }
}
