package view.sprites;

import view.MyColors;

public class FirePlaceSprite extends LoopingSprite {
    public FirePlaceSprite() {
        super("fireplace", "world_foreground.png", 0x69, 32);
        setColor1(MyColors.DARK_GRAY);
        setColor2(MyColors.YELLOW);
        setColor3(MyColors.RED);
        setColor4(MyColors.GRAY);
        setFrames(3);
    }
}
