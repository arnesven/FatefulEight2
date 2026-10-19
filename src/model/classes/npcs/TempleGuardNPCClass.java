package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.normal.CaptainClass;
import model.classes.Looks;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;

public class TempleGuardNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.LIGHT_YELLOW;
    private static final MyColors ROBE_DETAIL_COLOR = MyColors.YELLOW;

    public TempleGuardNPCClass() {
        super("Temple Guard");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL_COLOR);
        CaptainClass.putOnHalfHelm(characterAppearance);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL_COLOR);
    }
}
