package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;

public class BanditNPCClass extends NPCClass {
    public BanditNPCClass() {
        super("Bandit");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLightArmor(characterAppearance, MyColors.BROWN, MyColors.DARK_GRAY);
    }
}
