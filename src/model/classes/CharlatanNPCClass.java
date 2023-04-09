package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class CharlatanNPCClass extends NPCClass {
    protected CharlatanNPCClass() {
        super("Charlatan");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        MagicianClass.putOnTopHat(characterAppearance, MyColors.BLACK, MyColors.BEIGE);
        SpyClass.putOnFakeMustache(characterAppearance);
        Looks.putOnFancyRobe(characterAppearance, MyColors.TAN, MyColors.BLUE);
    }
}
