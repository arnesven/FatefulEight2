package model.states.duel;

import model.Model;

public class SpecialAttackDuelAction extends AttackMagicDuelAction {

    private int powerLevel = 0;
    private int powerPaid = 0;

    @Override
    protected void specificPrepare(Model model, MagicDuelEvent state, MagicDuelist performer) {
        super.specificPrepare(model, state, performer);
        if (isSuccess()) {
            this.powerPaid = performer.getGauge().getCurrentLevel();
            this.powerLevel = performer.getGauge().drainForSpecialAttack();
        }
    }

    @Override
    protected void execute(Model model, MagicDuelEvent state, MagicDuelAction opponentsAction, MagicDuelist opponent) {
        super.execute(model, state, opponentsAction, opponent);
        if (state.beamsAreLocked()) {
            getPerformer().addToPower(powerPaid / 2);
        }
    }

    @Override
    protected int getPowerCost() {
        return 0;
    }

    @Override
    public int getPowerLevel() {
        return this.powerLevel;
    }
}
