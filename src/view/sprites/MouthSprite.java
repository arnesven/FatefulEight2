package view.sprites;

import view.MyColors;

public class MouthSprite extends FaceSprite {
    public MouthSprite(int i, MyColors lipColor, MyColors hairColor) {
        super(i);
        setColor2(lipColor);
        setColor3(hairColor);
    }
}
