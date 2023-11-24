package view.sprites;

import model.races.Race;
import view.MyColors;

public class FancyNeckRight extends FaceAndClothesSprite {
    public FancyNeckRight(int num, MyColors color1, MyColors color2) {
        super(num, color1);
        setColor3(color2);
    }

    // Skeleton: 0x1B2
    // Normal: 0x192
    // Slender: 0xB2

}
