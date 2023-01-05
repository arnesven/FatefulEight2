package view.sprites;

import view.MyColors;

public class SignSprite extends Sprite16x16 {
    public SignSprite(String name, int num, MyColors foreground, MyColors background) {
        super(name + "sign", "world_foreground.png", num);
        setColor1(MyColors.BLACK);
        setColor2(background);
        setColor3(foreground);
    }
}
