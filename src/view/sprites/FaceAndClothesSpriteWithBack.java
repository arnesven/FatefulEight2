package view.sprites;

import view.MyColors;

import java.awt.*;

public class FaceAndClothesSpriteWithBack extends FaceAndClothesSprite {
    public FaceAndClothesSpriteWithBack(int i, MyColors color, MyColors backColor, MyColors feather) {
        super(i, color);
        setColor3(backColor);
        setColor4(feather);
    }

    public FaceAndClothesSpriteWithBack(int i, MyColors color, MyColors backColor) {
        this(i, color, backColor, MyColors.DARK_GREEN);
    }
}
