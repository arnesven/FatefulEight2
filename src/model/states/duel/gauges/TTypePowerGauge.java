package model.states.duel.gauges;

import model.states.duel.AIMatrices;
import model.states.duel.AIMatricesPresets;
import view.widget.PowerGaugeWidget;
import view.widget.TGaugeWidget;

public class TTypePowerGauge extends PowerGauge {
    private static final int[] LEVEL_INDICES = new int[]{13, 25};
    private static final int POWER_PER_SEGMENT = 4;
    private static final PowerGaugeSegment NARROW_SEGMENT_WITH_LINES = new NarrowSegmentWithLines();
    private static final PowerGaugeSegment NARROW_SEGMENT = new NarrowSegment();
    private static final String HELP_TEXT = "The T-Gauge (or Thin Gauge) is a good option for " +
            "duelist who employ an aggressive strategy. It only has two levels, but both of them are quick to reach.";
    private final int[] REQUIRED_STRENGTHS = new int[]{
            (LEVEL_INDICES[0]+1) * POWER_PER_SEGMENT,
            (LEVEL_INDICES[1]+1)*POWER_PER_SEGMENT};

    public TTypePowerGauge(boolean withGraphics) {
        super("T", LEVEL_INDICES, withGraphics);
    }

    @Override
    protected PowerGaugeWidget makeWidget() {
        return new TGaugeWidget(this);
    }

    @Override
    public int getMaxLevel() {
        return REQUIRED_STRENGTHS[REQUIRED_STRENGTHS.length-1];
    }

    @Override
    protected int powerRequiredForStrength(int strength) {
        return REQUIRED_STRENGTHS[strength-1];
    }

    @Override
    public PowerGaugeSegment getSegment(int segment) {
        if (segment == getLevelIndices(0)) {
            return NARROW_SEGMENT_WITH_LINES;
        }
        return NARROW_SEGMENT;
    }

    @Override
    public int getCurrentSegmentIndex() {
        return getCurrentLevel() / 4;
    }

    @Override
    public int getPowerPerSegment(int segmentIndex) {
        return POWER_PER_SEGMENT;
    }

    @Override
    protected AIMatrices getHighLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel6TGauge();
    }

    @Override
    protected AIMatrices getLowLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel1TGauge();
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }
}
