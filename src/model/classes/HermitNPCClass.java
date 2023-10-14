package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class HermitNPCClass extends NPCClass {
    protected HermitNPCClass() {
        super("Hermit");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.BEIGE);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return super.getAvatar(race, appearance);
    }
}
