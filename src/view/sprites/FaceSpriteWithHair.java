package view.sprites;

import view.MyColors;

public class FaceSpriteWithHair extends FaceSprite {
    public FaceSpriteWithHair(int number, MyColors hairColor, MyColors color3, MyColors color4) {
        super(number);
        this.setColor2(hairColor);
        this.setColor3(color3);
        this.setColor4(color4);
    }

    public FaceSpriteWithHair(int number, MyColors hairColor) {
        this(number, hairColor, MyColors.BLACK, MyColors.BLACK);
    }
}
