package model.items.spells;

import model.SpellHandler;
import model.classes.Skill;
import view.MyColors;

public abstract class SkillBoostingSpell extends AuxiliarySpell {
    public SkillBoostingSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
        SpellHandler.registerSkillBoostingSpell(this, getBoostingSkill());
    }

    protected abstract Skill getBoostingSkill();
}
