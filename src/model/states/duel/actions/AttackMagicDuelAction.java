package model.states.duel.actions;

import model.Model;
import model.states.duel.MagicDuelEvent;
import model.states.duel.MagicDuelist;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;

public class AttackMagicDuelAction extends MagicDuelAction {
    private boolean success;
    private boolean didDamage = false;

    @Override
    protected boolean isReactive() {
        return false;
    }

    @Override
    protected void specificPrepare(Model model, MagicDuelEvent state, MagicDuelist performer) {
        this.didDamage = false;
        this.success = performer.testMagicSkill(model, state, BASE_DIFFICULTY);
        if (!success) {
            getPerformer().setAnimation(new MiscastEffectSprite());
        } else {
            getPerformer().setAnimation(new CastingEffectSprite());
        }
    }

    protected boolean isSuccess() {
        return success;
    }

    @Override
    protected int getPowerCost() {
        return 7;
    }

    @Override
    protected boolean avertsAttack(Model model, MagicDuelEvent state,
                                   AttackMagicDuelAction attackMagicDuelAction, MagicDuelist opponent) {
        return success;
    }

    @Override
    protected void execute(Model model, MagicDuelEvent state, MagicDuelAction opponentsAction, MagicDuelist opponent) {
        if (!success) {
            return;
        }

        if (opponentsAction instanceof AttackMagicDuelAction &&
                opponentsAction.avertsAttack(model, state, this, getPerformer())) {
            state.lockBeams(getPowerLevel(), ((AttackMagicDuelAction) opponentsAction).getPowerLevel());
            return;
        }

        state.fireBeamAtOpponent(getPerformer(), getPowerLevel());
        this.didDamage = !opponentsAction.avertsAttack(model, state, this, getPerformer());
    }

    @Override
    public void wrapUp(Model model, MagicDuelEvent magicDuelEvent, MagicDuelist opponent) {
        if (didDamage) {
            int damage = getPowerLevel() + 1;
            String hit = "hit";
            if (damage > 1) {
                hit = "hits";
            }
            magicDuelEvent.textOutput(getPerformer().getName() + " dealt " + damage + " " + hit +
                    " to " + opponent.getName() + "!");
            opponent.takeDamage(damage);
        }
    }

    public int getPowerLevel() {
        return 0;
    }
}
