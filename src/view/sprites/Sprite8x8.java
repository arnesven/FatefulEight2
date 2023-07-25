package view.sprites;

import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class Sprite8x8 extends Sprite {
    public Sprite8x8(String name, String mapPath, int number, List<Sprite> layers) {
        super(name, mapPath, number % 16, number / 16, 8, 8, layers);
    }
    public Sprite8x8(String name, String mapPath, int number) {
        this(name, mapPath, number, new ArrayList<>());
    }

    public Sprite8x8(String name, String mapPath, int number,
                     MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        this(name, mapPath, number, new ArrayList<>());
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }
}
