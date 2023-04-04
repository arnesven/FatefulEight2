package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class MerchantNPCClass extends NPCClass {
    protected MerchantNPCClass() {
        super("Merchant");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnMerchantHat(characterAppearance, MyColors.RED, MyColors.GOLD);
        Looks.putOnFancyRobe(characterAppearance, MyColors.GOLD, MyColors.RED);
    }
}
