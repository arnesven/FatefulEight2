package view.sprites;

import model.races.Shoulders;
import view.MyColors;

public class FancyShoulderTopRight extends ClothesSprite {
    public FancyShoulderTopRight(MyColors color, MyColors detailColor, Shoulders shoulders) {
        super((shoulders == Shoulders.NARROW ? 0x7F : 0xB5), color, detailColor);
    }
}
