package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.npcs.NPCClass;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class BeautyNPCClass extends NPCClass {
    protected BeautyNPCClass() {
        super("Female Beauty");
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        if (appearance.getGender()) {
            return new AvatarSprite(race, 0x220, MyColors.BLUE, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
        }
        return new AvatarSprite(race, 0x220, MyColors.DARK_RED, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        if (characterAppearance.getGender()) {
            Looks.putOnLooseShirt(characterAppearance, MyColors.BLUE);
        } else {
            Looks.putOnLooseShirt(characterAppearance, MyColors.DARK_RED);
        }
    }
}
