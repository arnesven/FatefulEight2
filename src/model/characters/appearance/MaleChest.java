package model.characters.appearance;

import view.MyColors;
import view.sprites.*;

public class MaleChest extends TorsoChest {

    private final PortraitSprite CHEST_2 = new NakedFaceAndClothesSprite(0xC1);

    @Override
    public PortraitSprite makeNakedSprite() {
        return CHEST_2;
    }

    public PortraitSprite getTunicSprite(MyColors color) {
        return new TunicChestSprite(color);
    }

    public PortraitSprite getLooseShirtSprite(MyColors color) {
        return new WideChest(color);
    }

    @Override
    public PortraitSprite getFancySprite(MyColors color) {
        return new FancyChest(color);
    }
}
