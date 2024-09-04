package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;

public class GardenerNPCClass extends NPCClass {
    private static final MyColors COVERALLS_COLOR = MyColors.LIGHT_BLUE;
    private static final MyColors SHIRT_COLOR = MyColors.BEIGE;

    public GardenerNPCClass() {
        super("Gardener");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, SHIRT_COLOR);
        Looks.putOnApron(characterAppearance, COVERALLS_COLOR, SHIRT_COLOR);
    }
}
