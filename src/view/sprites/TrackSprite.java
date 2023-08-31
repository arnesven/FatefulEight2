package view.sprites;

import view.MyColors;

public class TrackSprite extends Sprite {
    public TrackSprite(int num, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        super("tracksprite" + num, "riding.png", num % 16, num / 16, 32, 32);
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }

    public TrackSprite(int num, MyColors color1, MyColors color2, MyColors color3) {
        this(num, color1, color2, color3, MyColors.BEIGE);
    }

    public TrackSprite(int num, MyColors color1, MyColors color2) {
        this(num, color1, color2, MyColors.ORANGE, MyColors.BEIGE);
    }
}
