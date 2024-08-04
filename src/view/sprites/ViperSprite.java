package view.sprites;

import view.MyColors;

public class ViperSprite extends LoopingSprite {
    public ViperSprite(String viper, String s, int i, MyColors color2, MyColors color3) {
        super(viper, s, i, 32);
        setColor1(MyColors.BLACK);
        setColor2(color2);
        setColor3(color3);
        setFrames(4);
    }

    public ViperSprite(String viper, String s, int i) {
        this(viper, s, i, MyColors.LIGHT_BLUE, MyColors.BEIGE);
    }
}
