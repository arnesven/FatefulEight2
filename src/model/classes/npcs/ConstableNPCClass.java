package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.MinerClass;
import view.MyColors;

public class ConstableNPCClass extends NPCClass {
    public ConstableNPCClass() {
        super("Constable");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.LIGHT_GRAY, MyColors.DARK_BLUE);
        MinerClass.putOnLampHelmet(characterAppearance);
    }
}
