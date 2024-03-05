package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.SpyClass;
import view.MyColors;

public class BarberNPCClass extends NPCClass {
    public BarberNPCClass() {
        super("Barber");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.WHITE);
        Looks.putOnApron(characterAppearance, MyColors.GRAY, MyColors.WHITE);
        SpyClass.putOnFakeMustache(characterAppearance, characterAppearance.getHairColor());
    }
}
