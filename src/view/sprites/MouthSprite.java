package view.sprites;

import view.MyColors;

public class MouthSprite extends FaceSprite {
    public MouthSprite(int i, MyColors hairColor) {
        super(i);
        setColor2(MyColors.DARK_RED);
        setColor3(hairColor);
    }
}
