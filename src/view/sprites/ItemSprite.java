package view.sprites;

import view.MyColors;

public class ItemSprite extends Sprite32x32 {
    public ItemSprite(int col, int row, MyColors color2, MyColors color3, MyColors color4) {
        super("item"+col+"-"+row+"c2"+color2.name()+"c3"+color3.name()+"c3"+color4.name(), "items.png", col+row*16,
                MyColors.GRAY, color2, color3, color4);
    }

    public ItemSprite(int col, int row, MyColors color2, MyColors color3) {
        this(col, row, color2, color3, MyColors.GOLD);
    }

    public ItemSprite(int col, int row) {
        this(col, row, MyColors.BROWN, MyColors.LIGHT_GRAY);
    }
}
