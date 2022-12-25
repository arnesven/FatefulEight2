package model.items.weapons;

import model.characters.GameCharacter;
import model.classes.Skill;

public abstract class WandWeapon extends Weapon {
    public WandWeapon(String name, int cost, Skill skill, int[] damageTable) {
        super(name, cost, skill, damageTable);
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public Skill getSkillToUse(GameCharacter gc) {
        if (gc.getRankForSkill(super.getSkillToUse(gc)) < gc.getRankForSkill(Skill.MagicAny)) {
            return Skill.MagicAny;
        }
        return super.getSkillToUse(gc);
    }
}
