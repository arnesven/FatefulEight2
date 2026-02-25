package view.sprites;

import view.MyColors;

public class FaceAndClothesSprite extends FaceSprite {
    public FaceAndClothesSprite(int i, MyColors color2, MyColors color4) {
        super(i);
        setColor2(color2);
        setColor3(MyColors.LIGHT_GRAY);
        setColor4(color4);
    }

    public FaceAndClothesSprite(int i, MyColors color) {
        this(i, color, MyColors.BLACK);
    }

    @Override
    public void setFlipHorizontal(boolean b) {
        super.setFlipHorizontal(b);
    }
}
