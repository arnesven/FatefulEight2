package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class ElderNPCClass extends NPCClass {
    public ElderNPCClass() {
        super("Elder");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.BEIGE, MyColors.ORANGE);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x220, MyColors.BEIGE, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }
}
