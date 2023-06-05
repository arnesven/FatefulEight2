package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.Condition;
import model.states.GameState;
import view.sprites.Sprite;

public abstract class SkillBoostingPotion extends Potion {

    private final Skill[] skills;
    private final String description;

    public SkillBoostingPotion(String name,Skill[] skills) {
        super(name, 24);
        this.skills = skills;
        StringBuilder bldr = new StringBuilder(", for the rest of the day +2 to: ");
        for (Skill s : skills) {
            bldr.append(s.getName() + ", ");
        }
        this.description = bldr.substring(0, bldr.length()-2);
    }

    @Override
    public String getShoppingDetails() {
        return description;
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addCondition(getBoostCondition());
        for (Skill s : skills) {
            gc.addTemporaryBonus(s, 2, true);
        }

        return gc.getName() + " has gained a " + getBoostCondition().getName() + ".";
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
