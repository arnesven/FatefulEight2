package view.sprites;

import view.MyColors;

import java.awt.*;

public class BanditSprite extends LoopingSprite {
    public BanditSprite(MyColors skinColor, int num) {
        super("banditavatar", "enemies.png", num, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.BROWN);
        setColor3(skinColor);
        setFrames(4);
    }

    public BanditSprite(MyColors skinColor) {
        this(skinColor, 0x04);
    }
}
