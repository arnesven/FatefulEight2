package view.sprites;

import model.races.Shoulders;
import view.MyColors;

public class FancyBottomLeftLeft extends ClothesSprite {
    public FancyBottomLeftLeft(MyColors color, MyColors detailColor, Shoulders shoulders) {
        super((shoulders == Shoulders.NARROW ? 0x8E : 0xC2), color, detailColor);
    }
}
