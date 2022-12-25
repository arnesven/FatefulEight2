package view.sprites;

import view.MyColors;

public class ClothesSpriteWithBack extends ClothesSprite {
    public ClothesSpriteWithBack(int i, MyColors color, MyColors backColor) {
        super(i, color);
        setColor3(backColor);
    }
}
