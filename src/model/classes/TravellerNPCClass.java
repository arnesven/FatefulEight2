package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.npcs.NPCClass;
import model.races.Race;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.ShortSleeve;
import view.MyColors;
import view.sprites.AvatarSprite;

public class TravellerNPCClass extends NPCClass {
    private static final MyColors TUNIC_COLOR = MyColors.DARK_RED;

    protected TravellerNPCClass() {
        super("Traveller");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFarmersHat(characterAppearance, MyColors.BROWN);
        Looks.putOnTunic(characterAppearance, TUNIC_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new ShortSleeve(TUNIC_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x220, MyColors.BEIGE, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }
}
