package view.sprites;

import view.MyColors;

public class FancyNeckRight extends FaceAndClothesSprite {
    public FancyNeckRight(MyColors color1, MyColors color2) {
        super(0xB2, color1);
        setColor3(color2);
    }
}
