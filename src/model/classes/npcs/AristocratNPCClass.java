package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.MagicianClass;
import view.MyColors;

public class AristocratNPCClass extends NPCClass {
    public AristocratNPCClass() {
        super("Aristocrat");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        MagicianClass.putOnTopHat(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY);
        Looks.putOnFancyRobe(characterAppearance, MyColors.DARK_BLUE, MyColors.DARK_GRAY);
    }
}
