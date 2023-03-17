package view.sprites;

import view.MyColors;

public class OverlayItemSprite extends ItemSprite {

    public OverlayItemSprite(int col, int row, MyColors color2, MyColors color3, MyColors color4, Sprite overlay) {
        super(col, row, color2, color3, color4);
        addToOver(overlay);
    }

    public OverlayItemSprite(int col, int row, Sprite overlay) {
        super(col, row);
        addToOver(overlay);
    }

}
