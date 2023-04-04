package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class ConstableNPCClass extends NPCClass {
    protected ConstableNPCClass() {
        super("Constable");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.LIGHT_GRAY, MyColors.DARK_BLUE);
        MinerClass.putOnLampHelmet(characterAppearance);
    }
}
