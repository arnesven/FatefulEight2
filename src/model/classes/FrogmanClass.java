package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.special.SpecialCharacterClass;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class FrogmanClass extends SpecialCharacterClass {
    protected FrogmanClass() {
        super("Frogman", "FRG", 4, 8, false, 0, new WeightedSkill[]{});
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) { }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        AvatarSprite spr = new AvatarSprite(race, 0x174, MyColors.DARK_GREEN, MyColors.LIGHT_YELLOW,
                appearance.getNormalHair(), CharacterAppearance.noHair());
        spr.setColor3(MyColors.BROWN);
        return spr;
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment();
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
