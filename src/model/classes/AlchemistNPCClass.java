package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class AlchemistNPCClass extends NPCClass {
    protected AlchemistNPCClass() {
        super("Alchemist");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.DARK_GREEN, MyColors.BLACK, MyColors.GOLD);
        Looks.putOnTunic(characterAppearance, MyColors.DARK_GREEN);
    }
}
