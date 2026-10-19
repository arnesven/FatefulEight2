package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.MagicianClass;
import model.classes.normal.SpyClass;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;

public class CharlatanNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.TAN;
    private static final MyColors ROBE_DETAIL_COLOR = MyColors.BLUE;

    public CharlatanNPCClass() {
        super("Charlatan");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        MagicianClass.putOnTopHat(characterAppearance, MyColors.BLACK, MyColors.BEIGE);
        SpyClass.putOnFakeMustache(characterAppearance, MyColors.BLACK);
        Looks.putOnFancyRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL_COLOR);
    }
}
