package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.MagicianClass;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;

public class AristocratNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.DARK_BLUE;
    private static final MyColors ROBE_DETAIL_COLOR = MyColors.DARK_GRAY;

    public AristocratNPCClass() {
        super("Aristocrat");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        MagicianClass.putOnTopHat(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY);
        Looks.putOnFancyRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public boolean coversEyebrows() {
        return true;
    }
}
