package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.GameState;
import view.MyColors;

public abstract class SkillBoostingSpell extends AuxiliarySpell {

    private GameCharacter target = null;

    public SkillBoostingSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    protected abstract Skill getBoostingSkill();

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.print("Who do you want to target with " + getName() + "? ");
        this.target = model.getParty().partyMemberInput(model, state, caster);
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        if (target == null) {
            target = caster;
        }
        state.println(target.getName() + " " + getBoostingSkill().getName() + " temporarily raised.");
        target.addTemporaryBonus(getBoostingSkill(), getBoostAmount() + getMasteryLevel(caster), false);
    }

    protected abstract int getBoostAmount();

    @Override
    public String getDescription() {
        return "Temporarily raises a character's rank in " + getBoostingSkill().getName() + ".";
    }

    public boolean boostsSkill(Skill skill) {
        return getBoostingSkill() == skill;
    }
}
