package view.sprites;

import view.MyColors;


public class ClothesSprite extends PortraitSprite {
    public ClothesSprite(int i, MyColors color, MyColors color3, MyColors color4) {
        super("clothes" + i, "clothes.png", i);
        this.setColor1(color);
        this.setColor2(MyColors.LIGHT_GRAY);
        this.setColor3(color3);
        this.setColor4(color4);
    }

    public ClothesSprite(int i, MyColors color, MyColors color3) {
        this(i, color, color3, MyColors.BLACK);
    }

    public ClothesSprite(int i, MyColors undershirtColor) {
        this(i, undershirtColor, MyColors.BLACK);
    }

    @Override
    public void setSkinColor(MyColors color) { }
}
