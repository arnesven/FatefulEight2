package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;

public class ProfessorNPCClass extends NPCClass {
    public ProfessorNPCClass() {
        super("Professor");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.BROWN, MyColors.DARK_BROWN);
    }
}
