package view.sprites;

import view.MyColors;

import java.util.List;

public class Sprite16x16 extends Sprite {
    public Sprite16x16(String name, String mapPath, int num) {
        this(name, mapPath, num, MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.CYAN);
    }

    public Sprite16x16(String name, String mapPath, int num, MyColors color1) {
        this(name, mapPath, num, color1, MyColors.WHITE, MyColors.RED, MyColors.CYAN);
    }

    public Sprite16x16(String name, String mapPath, int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super(name, mapPath, num % 16, num / 16, 16, 16);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }
}
