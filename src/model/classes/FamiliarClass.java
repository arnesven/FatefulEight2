package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class FamiliarClass extends SpecialCharacterClass {
    protected FamiliarClass() {
        super("Familiar", "Familiar", 4, 8, false, 0, new WeightedSkill[]{
                new WeightedSkill(Skill.UnarmedCombat, 5)
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {

    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0xB0, MyColors.BROWN, MyColors.DARK_BROWN, appearance.getNormalHair());
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
