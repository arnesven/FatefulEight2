package view.sprites;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class HoodRightTop extends FaceAndClothesSprite {
    public HoodRightTop(MyColors color, CharacterAppearance appearance) {
        super(appearance.hairOnTop() ? 0xD5 : 0xD2, color);
        setColor3(appearance.getHairColor());
    }
}
