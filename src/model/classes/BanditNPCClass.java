package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class BanditNPCClass extends NPCClass {
    protected BanditNPCClass() {
        super("Bandit");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLightArmor(characterAppearance, MyColors.BROWN, MyColors.DARK_GRAY);
    }
}
