package view.sprites;

import view.MyColors;

public class WaterSpraySprite extends LoopingSprite {
    public WaterSpraySprite(int startOnFrame) {
        super("watersoray", "quest.png", 0xD2, 32);
        setFrames(4);
        setColor1(MyColors.CYAN);
        setColor2(MyColors.WHITE);
        setCurrentFrame(startOnFrame);
        shiftUpPx(-2);
    }
}
