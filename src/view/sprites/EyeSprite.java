package view.sprites;

import view.MyColors;

public class EyeSprite extends FaceSprite {
    public EyeSprite(int i, MyColors haircolor, MyColors eyeballColor) {
        super(i);
        setColor2(haircolor);
        setColor3(eyeballColor);
    }
}
