package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.normal.CaptainClass;
import model.classes.Looks;
import view.MyColors;

public class TempleGuardNPCClass extends NPCClass {
    public TempleGuardNPCClass() {
        super("Temple Guard");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.LIGHT_YELLOW, MyColors.YELLOW);
        CaptainClass.putOnHalfHelm(characterAppearance);
    }
}
