package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class FarmerNPCClass extends NPCClass {
    protected FarmerNPCClass() {
        super("Farmer");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.BEIGE);
        Looks.putOnFarmersHat(characterAppearance, MyColors.BROWN);
    }
}
