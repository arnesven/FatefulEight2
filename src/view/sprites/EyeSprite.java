package view.sprites;

import view.MyColors;

public class EyeSprite extends FaceSprite {
    public EyeSprite(int i, MyColors haircolor) {
        super(i);
        setColor2(haircolor);
        setColor3(MyColors.WHITE);
    }
}
