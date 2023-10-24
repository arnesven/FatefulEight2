package view.sprites;

import model.races.Shoulders;
import view.MyColors;

public class FancyBottomRightRight extends ClothesSprite {
    public FancyBottomRightRight(MyColors color, MyColors detailColor, Shoulders shoulders) {
        super((shoulders == Shoulders.NARROW ? 0x8F : 0xC5), color, detailColor);
    }
}
