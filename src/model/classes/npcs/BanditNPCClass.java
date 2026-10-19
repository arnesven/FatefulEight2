package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.ShortSleeve;
import view.MyColors;

public class BanditNPCClass extends NPCClass {
    public BanditNPCClass() {
        super("Bandit");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLightArmor(characterAppearance, MyColors.BROWN, MyColors.DARK_GRAY);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new ShortSleeve(MyColors.DARK_BROWN);
    }
}
