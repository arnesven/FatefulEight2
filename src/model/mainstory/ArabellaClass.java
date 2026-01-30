package model.mainstory;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.npcs.NPCClass;
import view.MyColors;

public class ArabellaClass extends NPCClass {
    protected ArabellaClass() {
        super("Arabella");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_GREEN);
    }
}
