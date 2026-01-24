package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.NobleClass;
import view.MyColors;

public class RegentClass extends NobleClass {

    private final MyColors gemColor;

    public RegentClass(MyColors gemColor) {
        this.gemColor = gemColor;
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyRobe(characterAppearance, MyColors.DARK_RED, MyColors.WHITE);
        putOnCrown(characterAppearance, MyColors.GOLD, gemColor);
    }
}
