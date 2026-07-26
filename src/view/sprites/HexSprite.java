package view.sprites;

import view.MyColors;


public class HexSprite extends Sprite16x16 {
    public static final MyColors ROAD_COLOR = MyColors.BROWN;

    public HexSprite(String name, int number, MyColors color) {
        super("hex"+name, "world.png", number);
        this.setColor1(color);
        this.setColor2(MyColors.LIGHT_BLUE);
        this.setColor3(ROAD_COLOR);
    }
}
