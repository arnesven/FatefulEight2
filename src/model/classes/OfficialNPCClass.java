package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class OfficialNPCClass extends NPCClass {
    protected OfficialNPCClass() {
        super("Official");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnCap(characterAppearance, MyColors.GRAY);
        Looks.putOnFancyRobe(characterAppearance, MyColors.GRAY, MyColors.LIGHT_GRAY);
    }
}
