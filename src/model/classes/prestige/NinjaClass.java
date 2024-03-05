package model.classes.prestige;

import model.characters.appearance.CharacterAppearance;
import model.classes.normal.AssassinClass;
import model.classes.normal.ThiefClass;
import view.MyColors;

public class NinjaClass extends AssassinClass {

    public NinjaClass() {
        super("Ninja", "NJA", MyColors.DARK_PURPLE);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        super.putClothesOn(characterAppearance);
        ThiefClass.putOnThiefsMask(characterAppearance);
    }
}
