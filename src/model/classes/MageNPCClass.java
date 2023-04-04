package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class MageNPCClass extends NPCClass {
    protected MageNPCClass() {
        super("Mage");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.RED);
        Looks.putOnRobe(characterAppearance, MyColors.RED, MyColors.LIGHT_RED);
    }
}
