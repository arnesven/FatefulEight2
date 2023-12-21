package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;

public class NinjaClass extends AssassinClass {

    protected NinjaClass() {
        super("Ninja", "NJA", MyColors.DARK_PURPLE);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        super.putClothesOn(characterAppearance);
        ThiefClass.putOnThiefsMask(characterAppearance);
    }
}
