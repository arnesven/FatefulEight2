package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.special.SpecialCharacterClass;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.LimitedAvatarSprite;

public class FrogmanClass extends SpecialCharacterClass {
    protected FrogmanClass() {
        super("Frogman", "FRG", 4, 8, false, 0, new WeightedSkill[]{});
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) { }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new LimitedAvatarSprite(race, 0x374, MyColors.DARK_GREEN, MyColors.BROWN, MyColors.LIGHT_YELLOW, appearance.getNormalHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment();
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
