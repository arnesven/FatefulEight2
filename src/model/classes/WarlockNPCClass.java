package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class WarlockNPCClass extends NPCClass {
    protected WarlockNPCClass() {
        super("Warlock");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_PURPLE, MyColors.DARK_GREEN);
        Looks.putOnHood(characterAppearance, MyColors.DARK_PURPLE);
        Looks.putOnMask(characterAppearance, MyColors.DARK_PURPLE);
    }
}
