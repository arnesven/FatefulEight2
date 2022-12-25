package view.sprites;

import view.MyColors;

import java.awt.*;

public class BanditSprite extends LoopingSprite {
    public BanditSprite(MyColors skinColor) {
        super("banditavatar", "enemies.png", 0x04, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.BROWN);
        setColor3(skinColor);
        setFrames(4);
    }
}
