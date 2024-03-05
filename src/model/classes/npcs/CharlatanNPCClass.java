package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.MagicianClass;
import model.classes.normal.SpyClass;
import view.MyColors;

public class CharlatanNPCClass extends NPCClass {
    public CharlatanNPCClass() {
        super("Charlatan");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        MagicianClass.putOnTopHat(characterAppearance, MyColors.BLACK, MyColors.BEIGE);
        SpyClass.putOnFakeMustache(characterAppearance, MyColors.BLACK);
        Looks.putOnFancyRobe(characterAppearance, MyColors.TAN, MyColors.BLUE);
    }
}
