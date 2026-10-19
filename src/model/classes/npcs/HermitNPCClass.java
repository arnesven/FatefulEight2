package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.races.Race;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.ShortSleeve;
import view.MyColors;
import view.sprites.AvatarSprite;

public class HermitNPCClass extends NPCClass {
    public HermitNPCClass() {
        super("Hermit");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.BEIGE);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new ShortSleeve(MyColors.BEIGE);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x220, MyColors.BEIGE, race.getColor(),
                appearance.getNormalHair(), CharacterAppearance.noHair());
    }
}
