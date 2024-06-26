package view.sprites;

import view.MyColors;

public class Sprite32x16 extends Sprite {
    public Sprite32x16(String name, String mapPath, int num) {
        super(name, mapPath, num % 16, num / 16, 32, 16);
    }

    public Sprite32x16(String name, String mapPath, int num, MyColors color1,
                       MyColors color2, MyColors color3, MyColors color4) {
        this(name, mapPath, num);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }
}
