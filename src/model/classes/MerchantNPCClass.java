package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class MerchantNPCClass extends NPCClass {
    protected MerchantNPCClass() {
        super("Merchant");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnMerchantHat(characterAppearance, MyColors.RED, MyColors.GOLD);
        Looks.putOnFancyRobe(characterAppearance, MyColors.GOLD, MyColors.RED);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x20, MyColors.GOLD, appearance.getBackHairOnly());
    }
}
