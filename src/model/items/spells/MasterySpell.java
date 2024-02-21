package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.states.GameState;
import view.MyColors;

public abstract class MasterySpell extends Spell {

    public MasterySpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    @Override
    protected void successfullyCastHook(Model model, GameState state, GameCharacter caster) {
        if (masteriesEnabled()) {
            boolean updated = caster.getMasteries().registerSuccessfullCast(model, this);
            if (updated) {
                state.println(caster.getName() + " has achieved mastery level " +
                        getMasteryLevel(caster) + " for " + getName() + "!");
            }
        }
    }

    protected int getMasteryLevel(GameCharacter character) {
        return character.getMasteries().getMasteryLevel(this);
    }

    protected boolean masteriesEnabled() {
        return false;
    }

    public Integer[] getThresholds() {
        return new Integer[]{10, 25, 50, 100}; // TODO: something more reasonable like 10, 25, 50, 100
    }
}
