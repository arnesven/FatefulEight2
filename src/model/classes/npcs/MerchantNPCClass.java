package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.races.Race;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;
import view.sprites.AvatarSprite;

public class MerchantNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.GOLD;
    private static final MyColors ROBE_DETAIL_COLOR = MyColors.RED;

    public MerchantNPCClass() {
        super("Merchant");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnMerchantHat(characterAppearance, MyColors.RED, MyColors.GOLD);
        Looks.putOnFancyRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x260, MyColors.GOLD, race.getColor(), appearance.getBackHairOnly(), appearance.getFullBackHair());
    }

    @Override
    public boolean coversEyebrows() {
        return true;
    }
}
