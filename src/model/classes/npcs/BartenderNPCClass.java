package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.ShortSleeve;
import view.MyColors;

public class BartenderNPCClass extends NPCClass {
    private static final MyColors TUNIC_COLOR = MyColors.DARK_GRAY;

    public BartenderNPCClass() {
        super("Bartender");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, TUNIC_COLOR);
        Looks.putOnApron(characterAppearance, MyColors.BEIGE, MyColors.DARK_GRAY);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new ShortSleeve(TUNIC_COLOR);
    }
}
