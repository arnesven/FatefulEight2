package view.sprites;

import view.MyColors;

public class AttitudeSprite extends Sprite16x16 {
    public AttitudeSprite(int i, MyColors fill) {
        super("attitude" + i, "attitudes.png", i);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.BLUE);
        setColor3(fill);
    }
}
