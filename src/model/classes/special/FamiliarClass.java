package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class FamiliarClass extends SpecialCharacterClass {
    public FamiliarClass() {
        super("Familiar", "Familiar", 4, 8, false, 0, new WeightedSkill[]{
                new WeightedSkill(Skill.UnarmedCombat, 5)
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {

    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x360, MyColors.BROWN, MyColors.GOLD,
                MyColors.DARK_BROWN, appearance.getNormalHair(), CharacterAppearance.noHair());
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
