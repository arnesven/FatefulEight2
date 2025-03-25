package view.sprites;

import view.MyColors;

public class PirateHavenBobbingShipSprite extends LoopingSprite {
    public PirateHavenBobbingShipSprite() {
        super("piratehavenbobbingship", "world_foreground.png", 0xD3, 32, 32);
        setFrames(2);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.BEIGE);
        setColor3(MyColors.BROWN);
    }
}
