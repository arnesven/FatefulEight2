package view.sprites;

import view.MyColors;

public class ViperSprite extends LoopingSprite {
    public ViperSprite(String viper, String s, int i) {
        super(viper, s, i, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.LIGHT_BLUE);
        setColor3(MyColors.BEIGE);
        setFrames(4);
    }
}
