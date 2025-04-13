package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.states.GameState;
import util.MyLists;

import java.util.List;

public abstract class SkillBoostingPotion extends Potion {

    private final List<Skill> skills;
    private final String description;

    public SkillBoostingPotion(String name, List<Skill> skills) {
        super(name, 24);
        this.skills = skills;
        StringBuilder bldr = new StringBuilder(", for the rest of the day +" + getBoostAmount() + " to: ");
        bldr.append(MyLists.commaAndJoin(skills, Skill::getName));
        this.description = bldr.substring(0, bldr.length());
    }

    @Override
    public String getShoppingDetails() {
        return description;
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addCondition(getBoostCondition());
        for (Skill s : skills) {
            gc.addTemporaryBonus(s, getBoostAmount(), true);
        }

        return gc.getName() + " has gained a " + getBoostCondition().getName() + ".";
    }

    public int getBoostAmount() {
        return 2;
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return true;
    }

    protected abstract SkillBoostingCondition getBoostCondition();

    protected abstract class SkillBoostingCondition extends Condition {
        public SkillBoostingCondition(String conditionName, String shortName) {
            super(conditionName, shortName);
        }

        @Override
        protected boolean noCombatTurn() {
            return false;
        }

        @Override
        public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
            for (Skill s : skills) {
                ((GameCharacter) comb).removeTemporaryBonus(s);
            }
            removeYourself(comb);
        }

        protected abstract void removeYourself(Combatant comb);

    }

}
