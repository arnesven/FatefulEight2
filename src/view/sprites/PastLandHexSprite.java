package view.sprites;

import view.MyColors;

public class PastLandHexSprite extends Sprite16x16 {
    public PastLandHexSprite(String name, int number, MyColors color) {
        super("pasthex"+name, "world.png", number);
        this.setColor1(color);
        this.setColor2(MyColors.BLUE);
        this.setColor3(MyColors.BROWN);
    }
}
