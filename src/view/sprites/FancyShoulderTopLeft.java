package view.sprites;

import model.races.Shoulders;
import view.MyColors;

public class FancyShoulderTopLeft extends ClothesSprite {
    public FancyShoulderTopLeft(MyColors color, MyColors detailColor, Shoulders shoulders) {
        super((shoulders == Shoulders.NARROW ? 0x7E : 0xB4), color, detailColor);
    }
}
