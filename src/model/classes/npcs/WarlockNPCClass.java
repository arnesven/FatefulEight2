package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;

public class WarlockNPCClass extends NPCClass {
    public WarlockNPCClass() {
        super("Warlock");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_PURPLE, MyColors.DARK_GREEN);
        Looks.putOnHood(characterAppearance, MyColors.DARK_PURPLE);
    }

    @Override
    public boolean coversEars() {
        return true;
    }
}
