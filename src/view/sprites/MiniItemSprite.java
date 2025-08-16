package view.sprites;

import view.MyColors;

public class MiniItemSprite extends Sprite16x16 {
    public MiniItemSprite(int num, MyColors color2, MyColors color3) {
        super("miniitem" + num, "lotto.png", num);
        setColor2(color2);
        setColor3(color3);
    }
}
