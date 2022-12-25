package view.sprites;

import view.MyColors;
import view.sprites.FaceSprite;

import java.awt.*;

public class FaceAndClothesSprite extends FaceSprite {
    public FaceAndClothesSprite(int i, MyColors color) {
        super(i);
        setColor2(color);
        setColor3(MyColors.LIGHT_GRAY);
    }
}
