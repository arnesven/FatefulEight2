package view.sprites;

import view.MyColors;

public class FullPortraitSprite extends Sprite {
    public FullPortraitSprite(int column, int row) {
        super("silhouette", "silhouette.png", column, row, 56, 56);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.LIGHT_GRAY);
        setColor3(MyColors.GRAY);
    }
}
