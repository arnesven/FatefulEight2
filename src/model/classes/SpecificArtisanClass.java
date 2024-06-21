package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.normal.ArtisanClass;
import view.MyColors;

public class SpecificArtisanClass extends ArtisanClass {
    private final MyColors shirtColor;
    private final MyColors apronColor;

    public SpecificArtisanClass(MyColors shirtColor, MyColors apronColor) {
        this.shirtColor = shirtColor;
        this.apronColor = apronColor;
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, shirtColor);
        Looks.putOnApron(characterAppearance, apronColor, shirtColor);
    }
}
