package view.sprites;

import view.MyColors;

public class FaceSpriteWithHair extends FaceSprite {
    public FaceSpriteWithHair(int number, MyColors hairColor) {
        super(number);
        this.setColor2(hairColor);
    }
}
