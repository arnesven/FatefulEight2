package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.NobleClass;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class RegentClass extends NobleClass {

    private final MyColors gemColor;

    public RegentClass(MyColors gemColor) {
        this.gemColor = gemColor;
    }

    @Override
    public int getSpeed() {
        return -1;
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyRobe(characterAppearance, MyColors.DARK_RED, MyColors.WHITE);
        putOnCrown(characterAppearance, MyColors.GOLD, gemColor);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race,0xA0, MyColors.DARK_RED, race.getColor(), MyColors.WHITE,
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }
}
