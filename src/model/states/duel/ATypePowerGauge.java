package model.states.duel;

import view.widget.AGaugeWidget;
import view.widget.PowerGaugeWidget;

public class ATypePowerGauge extends PowerGauge {

    private static final int[] LEVEL_INDICES = new int[]{6, 13, 23};
    private static final int[] POWER_PER_SEGMENT = new int[]{12, 8, 4};
    private static final PowerGaugeSegment MEDIUM_SEGMENT_WITH_LINES = new MediumSegmentWithLines();
    private static final PowerGaugeSegment MEDIUM_SEGMENT = new MediumSegment();
    private static final PowerGaugeSegment WIDE_SEGMENT = new WideSegment();
    private static final PowerGaugeSegment WIDE_SEGMENT_WITH_LINES = new WideSegmentWithLines();
    private static final PowerGaugeSegment NARROW_SEGMENT = new NarrowSegment();
    private static final PowerGaugeSegment NARROW_SEGMENT_WITH_LINES = new NarrowSegmentWithLines();

    private final int[] requiredStrengths;

    public ATypePowerGauge(boolean withGraphics) {
        super(LEVEL_INDICES, withGraphics);

        requiredStrengths = new int[LEVEL_INDICES.length];
        requiredStrengths[0] = (LEVEL_INDICES[0] + 1) * POWER_PER_SEGMENT[0];
        requiredStrengths[1] = requiredStrengths[0] + (LEVEL_INDICES[1] - LEVEL_INDICES[0]) * POWER_PER_SEGMENT[1];
        requiredStrengths[2] = requiredStrengths[1] + (LEVEL_INDICES[2] - LEVEL_INDICES[1]) * POWER_PER_SEGMENT[2];
    }

    @Override
    protected PowerGaugeWidget makeWidget() {
        return new AGaugeWidget(this);
    }

    @Override
    public int getMaxLevel() {
        return requiredStrengths[LEVEL_INDICES.length-1];
    }

    @Override
    protected int powerRequiredForStrength(int strength) {
        return requiredStrengths[strength - 1];
    }

    @Override
    public PowerGaugeSegment getSegment(int segment) {
        if (segment == getLevelIndices(0)) {
            return WIDE_SEGMENT_WITH_LINES;
        }
        if (segment == getLevelIndices(1)) {
            return MEDIUM_SEGMENT_WITH_LINES;
        }
        if (segment < getLevelIndices(0)) {
            return WIDE_SEGMENT;
        }
        if (segment < getLevelIndices(1)) {
            return MEDIUM_SEGMENT;
        }
        return NARROW_SEGMENT;
    }

    @Override
    public int getCurrentSegmentIndex() {
        int current = getCurrentLevel();
        if (current < requiredStrengths[0]) {
            return current / POWER_PER_SEGMENT[0];
        }
        if (current < requiredStrengths[1]) {
            return LEVEL_INDICES[0] + (current - requiredStrengths[0]) / POWER_PER_SEGMENT[1] + 1;
        }
        return LEVEL_INDICES[1] + (current - requiredStrengths[1]) / POWER_PER_SEGMENT[2] + 1;
    }

    @Override
    public int getPowerPerSegment(int segmentIndex) {
        for (int level = 0; level < LEVEL_INDICES.length; ++level) {
            if (segmentIndex <= LEVEL_INDICES[level]) {
                return POWER_PER_SEGMENT[level];
            }
        }
        return POWER_PER_SEGMENT[POWER_PER_SEGMENT.length-1];
    }
}
