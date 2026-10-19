package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.ShortSleeve;
import view.MyColors;

public class MarshalNPCClass extends NPCClass {
    private static final MyColors SHIRT_COLOR = MyColors.DARK_RED;

    public MarshalNPCClass() {
        super("Marshal");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.LIGHT_GRAY, SHIRT_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new ShortSleeve(SHIRT_COLOR);
    }
}
