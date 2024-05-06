package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.npcs.NPCClass;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class MageNPCClass extends NPCClass {
    protected MageNPCClass() {
        super("Mage");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.RED, MyColors.DARK_RED, MyColors.DARK_RED);
        Looks.putOnRobe(characterAppearance, MyColors.RED, MyColors.LIGHT_RED);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x40, MyColors.RED, appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }
}
