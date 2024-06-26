package model.characters.appearance;

import view.MyColors;
import view.sprites.FaceAndClothesSprite;
import view.sprites.FaceAndClothesSpriteWithBack;

public class TopKnotHairStyle extends BaldHairStyle {
    private final MyColors bandColor;
    private final boolean onBald;

    public TopKnotHairStyle(MyColors color, boolean onBald, String description) {
        this.bandColor = color;
        this.onBald = onBald;
        super.setDescription(description);
    }

    @Override
    public void apply(AdvancedAppearance appearance) {
        FaceAndClothesSprite topKnot = new FaceAndClothesSpriteWithBack(0x87 + (onBald?0:1), appearance.getHairColor(), bandColor);
        FaceAndClothesSprite topKnot2 = new FaceAndClothesSprite(0x77, appearance.getHairColor());
        appearance.setSprite(3, 1, topKnot);
        appearance.setSprite(3, 0, topKnot2);
    }

    @Override
    public int getNormalHair() {
        return 0x21;
    }

    @Override
    public int getFullBackHair() {
        return 0x59;
    }
}
