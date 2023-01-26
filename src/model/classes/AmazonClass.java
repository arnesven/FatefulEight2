package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.WoodenSpear;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class AmazonClass extends CharacterClass {
    protected AmazonClass() {
        super("Amazon", "AMZ", 7, 7, false, 22,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 4),
                        new WeightedSkill(Skill.Bows, 4),
                        new WeightedSkill(Skill.Endurance, 3),
                        new WeightedSkill(Skill.MagicGreen, 2),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Polearms, 4),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkill(Skill.Sneak, 4),
                        new WeightedSkill(Skill.Survival, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnHideRight(characterAppearance, MyColors.BROWN);
        Looks.putOnHideLeft(characterAppearance, MyColors.BROWN);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x17, MyColors.BROWN, appearance.getNormalHair());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new WoodenSpear());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
