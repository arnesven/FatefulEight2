package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class TempleGuardNPCClass extends NPCClass {
    protected TempleGuardNPCClass() {
        super("Temple Guard");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.LIGHT_YELLOW, MyColors.YELLOW);
        CaptainClass.putOnHalfHelm(characterAppearance);
    }
}
