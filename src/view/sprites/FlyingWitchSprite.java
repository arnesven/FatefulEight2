package view.sprites;

import view.MyColors;

public class FlyingWitchSprite extends LoopingSprite {
    public FlyingWitchSprite(MyColors skinColor) {
        super("witchonbroom", "world.png", 0x70, 32);
        setFrames(8);
        setColor1(MyColors.BLACK);
        setColor2(skinColor);
        setColor3(MyColors.BROWN);
        setColor4(MyColors.LIGHT_YELLOW);
    }
}
