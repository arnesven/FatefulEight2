package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class FarmerNPCClass extends NPCClass {
    protected FarmerNPCClass() {
        super("Farmer");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.BEIGE);
        Looks.putOnFarmersHat(characterAppearance, MyColors.BROWN);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x87, MyColors.BEIGE, appearance.getNormalHair());
    }
}
