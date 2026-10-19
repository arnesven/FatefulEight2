package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;

public class OfficialNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.GRAY;
    private static final MyColors ROBE_DETAIL_COLOR = MyColors.LIGHT_GRAY;

    public OfficialNPCClass() {
        super("Official");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnCap(characterAppearance, MyColors.GRAY);
        Looks.putOnFancyRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        Looks.finalizeCap(appearance);
    }
}
