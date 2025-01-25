package view.sprites;

import view.MyColors;

public class Sprite16x32 extends Sprite {
    public Sprite16x32(String name, String map, int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super(name, map, num % 16, num / 16, 16, 32);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }
}
