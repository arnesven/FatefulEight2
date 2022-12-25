package view.sprites;

import view.MyColors;

import java.awt.*;

public class SeaHexSprite extends HexSprite {
    public SeaHexSprite(int num) {
        super("sea", num, MyColors.LIGHT_BLUE);
        this.setColor1(MyColors.LIGHT_BLUE);
        this.setColor2(MyColors.BLUE);
    }
}
