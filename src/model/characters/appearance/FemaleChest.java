package model.characters.appearance;

import view.MyColors;
import view.sprites.FaceAndClothesSprite;
import view.sprites.NakedFaceAndClothesSprite;
import view.sprites.PortraitSprite;

public class FemaleChest extends TorsoChest {

    private final PortraitSprite CHEST_1 = new NakedFaceAndClothesSprite(0x1B8);

    @Override
    public PortraitSprite makeNakedSprite() {
        return CHEST_1;
    }

    @Override
    public PortraitSprite getTunicSprite(MyColors color) {
        return new FaceAndClothesSprite(0x19F, color);
    }

    @Override
    public PortraitSprite getLooseShirtSprite(MyColors color) {
        return new FaceAndClothesSprite(0x1BF, color);
    }

    @Override
    public PortraitSprite getFancySprite(MyColors color) {
        return new FaceAndClothesSprite(0x1AF, color);
    }
}
