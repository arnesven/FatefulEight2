package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;

public class OfficialNPCClass extends NPCClass {
    public OfficialNPCClass() {
        super("Official");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnCap(characterAppearance, MyColors.GRAY);
        Looks.putOnFancyRobe(characterAppearance, MyColors.GRAY, MyColors.LIGHT_GRAY);
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        Looks.finalizeCap(appearance);
    }
}
