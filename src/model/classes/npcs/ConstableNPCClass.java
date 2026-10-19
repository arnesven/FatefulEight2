package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.MinerClass;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.PlainLongSleeve;
import view.MyColors;

public class ConstableNPCClass extends NPCClass {
    private static final MyColors UNDERSHIRT_COLOR = MyColors.DARK_BLUE;

    public ConstableNPCClass() {
        super("Constable");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.LIGHT_GRAY, UNDERSHIRT_COLOR);
        MinerClass.putOnLampHelmet(characterAppearance);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new PlainLongSleeve(UNDERSHIRT_COLOR);
    }
}
