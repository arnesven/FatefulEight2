package model.characters.appearance;

import model.races.Race;
import view.sprites.Sprite8x8;

public class EarringsDetail extends FaceDetail {
    private final int leftSprite;
    private final int rightSprite;

    public EarringsDetail(String name, int leftSprite, int rightSprite) {
        super(name);
        this.leftSprite = leftSprite;
        this.rightSprite = rightSprite;
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        if (!coversEars) {
            Sprite8x8 left = new Sprite8x8("earringleft", "clothes.png", leftSprite);
            left.setColor1(getColor());
            appearance.addSpriteOnTop(1, 3, left);
            Sprite8x8 right = new Sprite8x8("earringright", "clothes.png", rightSprite);
            right.setColor1(getColor());
            appearance.addSpriteOnTop(5, 3, right);
        }
    }
}
