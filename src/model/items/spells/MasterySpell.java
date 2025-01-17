package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.items.accessories.MasterRing;
import model.states.GameState;
import view.MyColors;

public abstract class MasterySpell extends Spell {

    public MasterySpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    @Override
    protected void successfullyCastHook(Model model, GameState state, GameCharacter caster) {
        if (masteriesEnabled()) {
            int masteryTimes = getMasteryTimes(caster);
            for (int i = 0; i < masteryTimes; ++i) {
                boolean updated = caster.getMasteries().registerSuccessfullCast(model, this);
                if (updated) {
                    state.println(caster.getName() + " has achieved mastery level " +
                            getMasteryLevel(caster) + " for " + getName() + "!");
                }
            }
        }
    }

    protected int getMasteryTimes(GameCharacter caster) {
        if (caster.getEquipment().getAccessory() != null) {
            return caster.getEquipment().getAccessory().getMasteryFactor();
        }
        return 1;
    }

    protected int getMasteryLevel(GameCharacter character) {
        if (!masteriesEnabled()) {
            throw new IllegalStateException("Masteries have not been enabled for this spell!");
        }
        return character.getMasteries().getMasteryLevel(this);
    }

    public boolean masteriesEnabled() {
        return false;
    }

    public Integer[] getThresholds() {
        return new Integer[]{10, 25, 50, 100};
    }
}
