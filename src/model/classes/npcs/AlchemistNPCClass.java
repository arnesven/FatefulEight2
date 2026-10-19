package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.PlainLongSleeve;
import view.MyColors;

public class AlchemistNPCClass extends NPCClass {
    private static final MyColors TUNIC_COLOR = MyColors.DARK_GREEN;

    public AlchemistNPCClass() {
        super("Alchemist");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.DARK_GREEN, MyColors.BLACK, MyColors.GOLD);
        Looks.putOnTunic(characterAppearance, TUNIC_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new PlainLongSleeve(TUNIC_COLOR);
    }

    @Override
    public boolean coversEyebrows() {
        return true;
    }
}
