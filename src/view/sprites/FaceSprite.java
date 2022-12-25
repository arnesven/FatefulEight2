package view.sprites;

import view.MyColors;

public class FaceSprite extends PortraitSprite {
    public FaceSprite(int i) {
        super("facesprite" + i, "face.png", i);
        setColor1(MyColors.LIGHT_RED);
        setColor2(MyColors.BLACK);
        setColor3(MyColors.BLACK);
    }

    @Override
    public void setSkinColor(MyColors color) {
        setColor1(color);
    }
}
