package view.sprites;

import view.MyColors;
import view.sprites.FaceSprite;

import java.awt.*;

public class FaceAndClothesSprite extends FaceSprite {
    public FaceAndClothesSprite(int i, MyColors color2, MyColors color4) {
        super(i);
        setColor2(color2);
        setColor3(MyColors.LIGHT_GRAY);
        setColor4(color4);
    }

    public FaceAndClothesSprite(int i, MyColors color) {
        this(i, color, MyColors.CYAN);
    }
}
