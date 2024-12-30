package model.states.duel.gauges;

public abstract class ComplexPowerGauge extends PowerGauge {

    private final int[] requiredStrengths;
    private final int[] levelIndices;
    private final int[] powerPerSegment;

    public ComplexPowerGauge(String name, int[] levelIndices, int[] powerPerSegment, boolean withGraphics) {
        super(name, levelIndices, withGraphics);
        this.levelIndices = levelIndices;
        this.powerPerSegment = powerPerSegment;

        requiredStrengths = new int[levelIndices.length];
        requiredStrengths[0] = (levelIndices[0] + 1) * powerPerSegment[0];

        for (int i = 1; i < requiredStrengths.length; ++i) {
            requiredStrengths[i] = requiredStrengths[i-1] + (levelIndices[i] - levelIndices[i-1]) * powerPerSegment[i];
        }
    }

    @Override
    public int getMaxLevel() {
        return requiredStrengths[requiredStrengths.length-1];
    }

    @Override
    protected int powerRequiredForStrength(int strength) {
        return requiredStrengths[strength-1];
    }

    @Override
    public int getCurrentSegmentIndex() {
        int current = getCurrentLevel();
        if (current < requiredStrengths[0]) {
            return current / powerPerSegment[0];
        }
        for (int i = 1; i < requiredStrengths.length; ++i) {
            if (current < requiredStrengths[i]) {
                return levelIndices[i-1] + (current - requiredStrengths[i-1]) / powerPerSegment[i] + 1;
            }
        }

        return levelIndices[levelIndices.length-1] +
                (current - requiredStrengths[requiredStrengths.length-1]) / powerPerSegment[powerPerSegment.length-1] + 1;
    }


    @Override
    public int getPowerPerSegment(int segmentIndex) {
        for (int level = 0; level < levelIndices.length; ++level) {
            if (segmentIndex <= levelIndices[level]) {
                return powerPerSegment[level];
            }
        }
        return powerPerSegment[powerPerSegment.length-1];
    }
}
