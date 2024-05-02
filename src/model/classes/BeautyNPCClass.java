package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.npcs.NPCClass;
import view.MyColors;

public class BeautyNPCClass extends NPCClass {
    protected BeautyNPCClass() {
        super("Female Beauty");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        if (characterAppearance.getGender()) {
            Looks.putOnLooseShirt(characterAppearance, MyColors.BLUE);
        } else {
            Looks.putOnLooseShirt(characterAppearance, MyColors.DARK_RED);
        }
    }
}
