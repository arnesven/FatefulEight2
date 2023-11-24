package view.sprites;

import view.MyColors;

public class FancyNeckLeft extends FaceAndClothesSprite {
    public FancyNeckLeft(int num, MyColors color1, MyColors color2) {
        super(num, color1);
        setColor3(color2);
    }

    // Skeleton : 0x1A2
    // Normal: 0x182
    // Slender: 0xA2
}
