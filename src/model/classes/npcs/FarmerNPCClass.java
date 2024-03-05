package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class FarmerNPCClass extends NPCClass {
    public FarmerNPCClass() {
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
