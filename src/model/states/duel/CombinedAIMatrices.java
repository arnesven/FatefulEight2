package model.states.duel;

import util.MyRandom;

public class CombinedAIMatrices extends AIMatrices {
    private final int level;
    private final AIMatrices lowMatrices;
    private final AIMatrices highMatrices;

    public CombinedAIMatrices(int level, AIMatrices lowMatrices, AIMatrices highMatrices) {
        super(makeBaselineNormalMatrix(), makeBaselineAttackMatrix(),
                makeBaselineShieldMatrix(), makeBaselineBeamMatrix());
        this.level = level;
        this.lowMatrices = lowMatrices;
        this.highMatrices = highMatrices;
    }

    @Override
    public int rollOnNormalTable(int opponentPowerStrength, int ownStrength) {
        if (MyRandom.rollD6() < level) {
            return highMatrices.rollOnNormalTable(opponentPowerStrength, ownStrength);
        }
        return lowMatrices.rollOnNormalTable(opponentPowerStrength, ownStrength);
    }

    @Override
    public int rollOnAttackTypeTable(int opponentStrength, int ownStrength) {
        if (MyRandom.rollD6() < level) {
            return highMatrices.rollOnAttackTypeTable(opponentStrength, ownStrength);
        }
        return super.rollOnAttackTypeTable(opponentStrength, ownStrength);
    }

    @Override
    public int rollOnShieldTable(int opponentPowerStrength, int ownStrength) {
        if (MyRandom.rollD6() < level) {
            return highMatrices.rollOnShieldTable(opponentPowerStrength, ownStrength);
        }
        return super.rollOnShieldTable(opponentPowerStrength, ownStrength);
    }

    @Override
    public int rollOnBeamTable(int shift, int ownStrength) {
        if (MyRandom.rollD6() < level) {
            return highMatrices.rollOnBeamTable(shift, ownStrength);
        }
        return super.rollOnBeamTable(shift, ownStrength);
    }
}
