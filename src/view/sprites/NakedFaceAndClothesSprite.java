package view.sprites;

import view.MyColors;

public class NakedFaceAndClothesSprite extends FaceAndClothesSprite {
    public NakedFaceAndClothesSprite(int i) {
        super(i, MyColors.CYAN);
    }

    @Override
    public void setSkinColor(MyColors color) {
        super.setSkinColor(color);
        setColor2(color);
    }
}
