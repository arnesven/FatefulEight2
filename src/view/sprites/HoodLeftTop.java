package view.sprites;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class HoodLeftTop extends FaceAndClothesSprite {
    public HoodLeftTop(MyColors color, CharacterAppearance appearance) {
        super(appearance.hairOnTop() ? 0xD3 : 0xD0, color);
        setColor3(appearance.getHairColor());
    }
}
