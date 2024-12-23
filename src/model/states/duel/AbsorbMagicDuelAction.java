package model.states.duel;

import model.Model;
import util.MyRandom;
import view.sprites.AbsorbMagicAnimationSprite;

public class AbsorbMagicDuelAction extends MagicDuelAction {
    private boolean absorbed = false;
    private int amount;

    @Override
    protected boolean isReactive() {
        return true;
    }

    @Override
    protected void specificPrepare(Model model, MagicDuelEvent state, MagicDuelist performer) {
        // Nothing to do.
    }

    @Override
    protected int getPowerCost() {
        return 11;
    }

    @Override
    protected boolean avertsAttack(Model model, MagicDuelEvent state,
                                   AttackMagicDuelAction attackMagicDuelAction, MagicDuelist opponent) {
        boolean success = getPerformer().testMagicSkill(model, state,
                BASE_DIFFICULTY + attackMagicDuelAction.getPowerLevel());
        if (success) {
            this.absorbed = true;
            for (int i = 0; i < attackMagicDuelAction.getPowerLevel() + 1; ++i) {
                this.amount += (MyRandom.rollD6() + MyRandom.rollD6() + MyRandom.rollD6());
            }
            state.println(getPerformer().getName() + " successfully absorbed the power of " +
                    opponent.getName() + "'s attack.");
        }
        return success;
    }

    @Override
    public void wrapUp(Model model, MagicDuelEvent magicDuelEvent, MagicDuelist opponent) {
        if (absorbed) {
            getPerformer().addToPower(this.amount);
            getPerformer().setAnimation(new AbsorbMagicAnimationSprite());
        }
    }
}
