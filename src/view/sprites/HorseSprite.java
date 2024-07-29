package view.sprites;

import view.MyColors;

public class HorseSprite extends Sprite {
    private final MyColors color1;
    private final MyColors color2;
    private final MyColors color3;
    private final MyColors color4;

    public HorseSprite(int col, int row, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super("horse", "horses.png", col, row, 64, 64);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
    }

    public Sprite16x16 getMini() {
        return new Sprite16x16("horsemini", "horses.png", 0x0C + getRow() * 0x10 + getColumn(),
                color1, color2, color3, color4);
    }
}
