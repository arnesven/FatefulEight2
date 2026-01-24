package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;

public class MarshalNPCClass extends NPCClass {
    public MarshalNPCClass() {
        super("Marshal");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.LIGHT_GRAY, MyColors.DARK_RED);
    }
}
