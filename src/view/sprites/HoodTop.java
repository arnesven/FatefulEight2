package view.sprites;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class HoodTop extends FaceAndClothesSprite {
    public HoodTop(MyColors color, CharacterAppearance appearance) {
        super(appearance.hairOnTop() ? 0xD4 : 0xD1, color);
        setColor3(appearance.getHairColor());
    }
}
