package model.states.duel;

import model.Model;

public class MatrixDuelistController implements DuelistController {

    private final MagicDuelist duelist;
    private AIMatrices matrices;
    private final MagicDuelAction normalAttackAction = new NormalAttackDuelAction();
    private final MagicDuelAction specialAttackAction = new SpecialAttackDuelAction();
    private final MagicDuelAction absorbedAttackAction = new AbsorbMagicDuelAction();
    private final MagicDuelAction[] shieldActions = new MagicDuelAction[]{
            new ShieldMagicDuelAction(0),
            new ShieldMagicDuelAction(1),
            new ShieldMagicDuelAction(2),
            new ShieldMagicDuelAction(3)};

    public MatrixDuelistController(MagicDuelist magicDuelist, AIMatrices matrices) {
        this.duelist = magicDuelist;
        this.matrices = matrices;
    }

    @Override
    public MagicDuelAction selectNormalTurnAction(Model model, MagicDuelEvent state) {
        int x = findNormalActionType(opponentStrength(state));
        if (x == 0) {
            return attackAction(model, state);
        }
        if (x == 2) {
            return shieldAction(model, state);
        }
        return absorbedAttackAction;
    }

    private int opponentStrength(MagicDuelEvent state) {
        if (state.getOpponentPowerStrength(duelist) > 0) {
            return 1;
        }
        return 0;
    }

    private MagicDuelAction attackAction(Model model, MagicDuelEvent state) {
        if (!duelist.canDoSpecialAttack()) {
            return normalAttackAction;
        }
        int x = findAttackType(opponentStrength(state));
        if (x == 0) {
            return normalAttackAction;
        }
        return specialAttackAction;
    }

    private MagicDuelAction shieldAction(Model model, MagicDuelEvent state) {
        return shieldActions[findShieldLevel(opponentStrength(state))];
    }

    private int findAttackType(int opponentPowerStrength) {
        return matrices.rollOnAttackTypeTable(opponentPowerStrength,
                duelist.getGauge().getCurrentStrength()-1);
    }

    private int findNormalActionType(int opponentPowerStrength) {
        return matrices.rollOnNormalTable(opponentPowerStrength, duelist.getGauge().getCurrentStrength());
    }

    private int findShieldLevel(int opponentPowerStrength) {
        return matrices.rollOnShieldTable(opponentPowerStrength, duelist.getGauge().getCurrentStrength());
    }

    @Override
    public BeamOptions selectBeamTurnAction(Model model, MagicDuelEvent magicDuelEvent) {
        return BeamOptions.values()[matrices.rollOnBeamTable(magicDuelEvent.getLockedBeamShift() + 2,
                duelist.getGauge().getCurrentStrength())];
    }

    public void setMatrix(AIMatrices matrix) {
        this.matrices = matrix;
    }
}
