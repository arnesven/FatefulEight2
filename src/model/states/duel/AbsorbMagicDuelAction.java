package model.states.duel;

import model.Model;
import util.MyRandom;
import view.sprites.AbsorbMagicAnimationSprite;

public class AbsorbMagicDuelAction extends MagicDuelAction {
    private static final int DICE_PER_POWER_LEVEL = 6;
    private boolean absorbed = false;
    private int amount;

    @Override
    protected boolean isReactive() {
        return true;
    }

    @Override
    protected void specificPrepare(Model model, MagicDuelEvent state, MagicDuelist performer) {
        absorbed = false;
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
            this.amount += (MyRandom.rollD6() + MyRandom.rollD6() + MyRandom.rollD6());
            for (int i = 1; i <= attackMagicDuelAction.getPowerLevel(); ++i) {
                for (int j = 0; j < DICE_PER_POWER_LEVEL; ++j) {
                    this.amount += MyRandom.rollD6();
                }
            }
            state.textOutput(getPerformer().getName() + " successfully absorbed the power of " +
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
