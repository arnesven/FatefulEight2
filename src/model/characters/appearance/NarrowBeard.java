package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.FaceAndClothesSprite;
import view.sprites.FaceSprite;
import view.sprites.FilledBlockSprite;

public class NarrowBeard extends Beard {
    private final MyColors lineColor;
    public NarrowBeard(MyColors color) {
        super(0, 0x45);
        this.lineColor = color;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        setSpriteOnTop(appearance, 0x135, 2, 4, lineColor);
        setSpriteOnTop(appearance, 0x136, 4, 4, lineColor);
        setSpriteOnTop(appearance, 0x145, 2, 5, lineColor);
        appearance.setSprite(3, 5, new FilledBlockSprite(appearance.getFacialHairColor()));
        setSpriteOnTop(appearance, 0x146, 4, 5, lineColor);
        setSpriteOnTop(appearance, 0x155, 3, 6, lineColor);
    }

}
