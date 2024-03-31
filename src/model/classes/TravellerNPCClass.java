package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.npcs.NPCClass;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class TravellerNPCClass extends NPCClass {
    protected TravellerNPCClass() {
        super("Traveller");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFarmersHat(characterAppearance, MyColors.BROWN);
        Looks.putOnTunic(characterAppearance, MyColors.DARK_RED);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x87, MyColors.BEIGE, appearance.getNormalHair());
    }
}
