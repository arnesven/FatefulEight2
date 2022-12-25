package view.sprites;

import view.MyColors;

import java.awt.*;

public class NakedClothesSprite extends ClothesSprite {
    public NakedClothesSprite(int i) {
        super(i, MyColors.CYAN);
    }

    @Override
    public void setSkinColor(MyColors color) {
        super.setSkinColor(color);
        setColor1(color);
    }
}
