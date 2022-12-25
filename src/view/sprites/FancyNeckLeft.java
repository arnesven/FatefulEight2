package view.sprites;

import view.MyColors;

public class FancyNeckLeft extends FaceAndClothesSprite {
    public FancyNeckLeft(MyColors color1, MyColors color2) {
        super(0xA2, color1);
        setColor3(color2);
    }
}
