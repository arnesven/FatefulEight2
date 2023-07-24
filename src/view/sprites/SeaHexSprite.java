package view.sprites;

import view.MyColors;


public class SeaHexSprite extends LoopingSprite {
    public SeaHexSprite(int num) {
        super("sea", "world.png", num,16, 16);
        this.setColor1(MyColors.LIGHT_BLUE);
        this.setColor2(MyColors.CYAN);
        setFrames(4);
        setDelay(16);
    }
}
