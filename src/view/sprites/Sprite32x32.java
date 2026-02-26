package view.sprites;

import view.MyColors;

import java.util.List;

public class Sprite32x32 extends Sprite {

    public Sprite32x32(String name, String mapPath, int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super(name, mapPath, num % 16, num / 16, 32, 32);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }

    public Sprite32x32(String name, String mapPath, int num, MyColors color1, List<Sprite> sprites) {
        super(name, mapPath, num % 16, num / 16, 32, 32, sprites);
        setColor1(color1);
    }

    public Sprite32x32(String name, String mapPath, int num, MyColors color1, MyColors color2, MyColors color3, List<Sprite> sprites) {
        super(name, mapPath, num % 16, num / 16, 32, 32, sprites);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
    }

    public Sprite32x32(String name, String mapPath, int num, MyColors color1, MyColors color2, MyColors color3) {
        this(name, mapPath, num, color1, color2, color3, MyColors.YELLOW);
    }
}

