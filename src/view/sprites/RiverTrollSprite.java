package view.sprites;

import view.MyColors;

public class RiverTrollSprite extends LoopingSprite {
    public RiverTrollSprite(int num, MyColors color3) {
        super("rivertrollenemy"+num, "enemies.png", num, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.LIGHT_GRAY);
        setColor3(color3);
        setColor4(MyColors.DARK_GREEN);
        setFrames(4);
    }
}
