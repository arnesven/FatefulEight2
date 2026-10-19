package model.mainstory;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.npcs.NPCClass;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;

public class ArabellaClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.DARK_GRAY;
    private static final MyColors ROBE_DETAIL_COLOR = MyColors.DARK_GREEN;

    protected ArabellaClass() {
        super("Arabella");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL_COLOR);
    }
}
