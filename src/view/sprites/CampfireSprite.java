package view.sprites;

import view.MyColors;

public class CampfireSprite extends LoopingSprite {
    public CampfireSprite() {
        super("campfire", "dungeon.png", 0x100, 32, 32);
        setColor1(MyColors.BROWN);
        setColor2(MyColors.WHITE);
        setColor3(MyColors.YELLOW);
        setColor4(MyColors.ORANGE);
        setFrames(4);
    }
}
