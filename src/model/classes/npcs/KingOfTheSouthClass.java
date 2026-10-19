package model.classes.npcs;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.NobleClass;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.NoSleeveAtAll;
import view.MyColors;

public class KingOfTheSouthClass extends NPCClass {
    public KingOfTheSouthClass() {
        super("King of the South");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnHideRight(characterAppearance, MyColors.BROWN);
        Looks.putOnHideLeft(characterAppearance, MyColors.BROWN);
        NobleClass.putOnCrown(characterAppearance, MyColors.LIGHT_GRAY, MyColors.GREEN);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new NoSleeveAtAll();
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        Looks.putOnNecklace(appearance);
    }
}
