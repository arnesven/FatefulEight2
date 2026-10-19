package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.races.Race;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;
import view.sprites.AvatarSprite;

public class ElderNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.BEIGE;
    private static final MyColors ROBE_DETAIL_COLOR = MyColors.ORANGE;

    public ElderNPCClass() {
        super("Elder");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x220, MyColors.BEIGE, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }
}
