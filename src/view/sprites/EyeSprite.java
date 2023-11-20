package view.sprites;

import view.MyColors;

public class EyeSprite extends FaceSprite {
    public EyeSprite(int i, MyColors haircolor, MyColors eyeballColor, MyColors mascara) {
        super(i);
        setColor2(haircolor);
        setColor3(eyeballColor);
        setColor4(mascara);
    }
}
