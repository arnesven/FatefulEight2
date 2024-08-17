package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class MerchantNPCClass extends NPCClass {
    public MerchantNPCClass() {
        super("Merchant");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnMerchantHat(characterAppearance, MyColors.RED, MyColors.GOLD);
        Looks.putOnFancyRobe(characterAppearance, MyColors.GOLD, MyColors.RED);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x168, MyColors.GOLD, race.getColor(), appearance.getBackHairOnly(), appearance.getFullBackHair());
    }
}
