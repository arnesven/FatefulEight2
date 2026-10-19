package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.AssassinClass;
import model.classes.npcs.NPCClass;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;

public class CultistNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.DARK_BLUE;
    private static final MyColors ROBE_DETAIL = MyColors.DARK_GREEN;

    public CultistNPCClass() {
        super("Cultist");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL);
        Looks.putOnHood(characterAppearance, ROBE_COLOR); // TODO: make look like warlock?
        Looks.putOnMask(characterAppearance, ROBE_COLOR);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL);
    }

    @Override
    public boolean coversEars() {
        return true;
    }

    @Override
    public boolean showFacialHair() {
        return false;
    }
}
