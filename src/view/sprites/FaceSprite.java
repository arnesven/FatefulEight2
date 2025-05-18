package view.sprites;

import view.MyColors;

public class FaceSprite extends PortraitSprite {

    public FaceSprite(String name, String mapName, int num) {
        super(name, mapName, num);
        setColor1(MyColors.LIGHT_RED);
        setColor2(MyColors.BLACK);
        setColor3(MyColors.BLACK);
    }

    public FaceSprite(int i) {
       this("facesprite" + i, "face.png", i);
    }

    @Override
    public void setSkinColor(MyColors color) {
        setColor1(color);
    }
}
