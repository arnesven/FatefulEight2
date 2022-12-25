package view.sprites;

import view.MyColors;

import java.awt.*;

public class HexSprite extends Sprite16x16 {
    public HexSprite(String name, int number, MyColors color) {
        super("hex"+name, "world.png", number);
        this.setColor1(color);
        this.setColor2(MyColors.LIGHT_BLUE);
        this.setColor3(MyColors.BROWN);
    }
}
