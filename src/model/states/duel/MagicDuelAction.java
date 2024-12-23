package model.states.duel;

import model.Model;

public abstract class MagicDuelAction {
    protected static final int BASE_DIFFICULTY = 5;

    private MagicDuelist performer;

    protected abstract boolean isReactive();

    protected abstract void specificPrepare(Model model, MagicDuelEvent state, MagicDuelist performer);

    public void prepare(Model model, MagicDuelEvent state, MagicDuelist performer) {
        this.performer = performer;
        performer.addToPower(-getPowerCost());
        specificPrepare(model, state, performer);
    }

    protected abstract int getPowerCost();

    protected MagicDuelist getPerformer() {
        return performer;
    }

    public void resolve(Model model, MagicDuelEvent state, MagicDuelAction opponentAction) {
        if (isReactive() && opponentAction.isReactive()) {
            return;
        }
        if (isReactive()) {
            opponentAction.execute(model, state, this, getPerformer());
        } else {
            execute(model, state, opponentAction, opponentAction.getPerformer());
        }
    }

    protected void execute(Model model, MagicDuelEvent state,
                            MagicDuelAction opponentsAction, MagicDuelist opponent) {
    }

    protected boolean avertsAttack(Model model, MagicDuelEvent state,
                                   AttackMagicDuelAction attackMagicDuelAction,
                                   MagicDuelist opponent) {
        return false;
    }

    public void wrapUp(Model model, MagicDuelEvent magicDuelEvent, MagicDuelist opponent) {

    }
}
