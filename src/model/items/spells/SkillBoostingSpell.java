package model.items.spells;

import model.Model;
import model.SpellHandler;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.GameState;
import view.MyColors;

public abstract class SkillBoostingSpell extends AuxiliarySpell {

    private GameCharacter target;

    public SkillBoostingSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
        SpellHandler.registerSkillBoostingSpell(this, getBoostingSkill());
    }

    protected abstract Skill getBoostingSkill();

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.print("Who do you want to target with " + getName() + "? ");
        this.target = model.getParty().partyMemberInput(model, state, caster);
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        state.println(target.getName() + " " + getBoostingSkill().getName() + " temporarily raised.");
        target.addTemporaryBonus(getBoostingSkill(), getBoostAmount());
    }

    protected abstract int getBoostAmount();

    @Override
    public String getDescription() {
        return "Temporarily raises a character's rank in " + getBoostingSkill().getName() + ".";
    }
}