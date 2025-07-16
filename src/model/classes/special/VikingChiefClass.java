package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.CaptainClass;
import model.classes.prestige.VikingClass;
import view.MyColors;

public class VikingChiefClass extends VikingClass {

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        CaptainClass.putOnHalfHelm(characterAppearance);
        putOnHorns(characterAppearance, MyColors.DARK_RED);
        Looks.putOnArmor(characterAppearance, MyColors.GRAY, MyColors.DARK_RED);
    }
}
