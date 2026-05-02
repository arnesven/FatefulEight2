package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;

public class BartenderNPCClass extends NPCClass {
    public BartenderNPCClass() {
        super("Bartender");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.DARK_GRAY);
        Looks.putOnApron(characterAppearance, MyColors.BEIGE, MyColors.DARK_GRAY);
    }
}
