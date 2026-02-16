package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;

public class AlchemistNPCClass extends NPCClass {
    public AlchemistNPCClass() {
        super("Alchemist");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.DARK_GREEN, MyColors.BLACK, MyColors.GOLD);
        Looks.putOnTunic(characterAppearance, MyColors.DARK_GREEN);
    }

    @Override
    public boolean coversEyebrows() {
        return true;
    }
}
