package model.states.duel.gauges;

import model.states.duel.AIMatrices;
import model.states.duel.AIMatricesPresets;
import util.MyStrings;
import view.widget.AGaugeWidget;
import view.widget.PowerGaugeWidget;

public class ATypePowerGauge extends ComplexPowerGauge {

    private static final int[] LEVEL_INDICES = new int[]{6, 13, 23};
    private static final int[] POWER_PER_SEGMENT = new int[]{12, 8, 4};
    private static final PowerGaugeSegment MEDIUM_SEGMENT_WITH_LINES = new MediumSegmentWithLines();
    private static final PowerGaugeSegment MEDIUM_SEGMENT = new MediumSegment();
    private static final PowerGaugeSegment WIDE_SEGMENT = new WideSegment();
    private static final PowerGaugeSegment WIDE_SEGMENT_WITH_LINES = new WideSegmentWithLines();
    private static final PowerGaugeSegment NARROW_SEGMENT = new NarrowSegment();
    private static final PowerGaugeSegment NARROW_SEGMENT_WITH_LINES = new NarrowSegmentWithLines();
    private static final String HELP_TEXT = "The A-Gauge (or Advanced Gauge) is a " + MyStrings.numberWord(LEVEL_INDICES.length) +
            " leveled gauge which holds slightly less power than the B-Gauge. Therefore it is a little faster at reaching " +
            "the top level. However, due to its shape, it is somewhat slower at reaching the lower levels than most other gauges.";

    public ATypePowerGauge(boolean withGraphics) {
        super("A", LEVEL_INDICES, POWER_PER_SEGMENT, withGraphics);
    }

    @Override
    protected PowerGaugeWidget makeWidget() {
        return new AGaugeWidget(this);
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
    protected AIMatrices getHighLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel6AGauge();
    }

    @Override
    protected AIMatrices getLowLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel1AGauge();
    }

    @Override
    public PowerGauge copy() {
        return new ATypePowerGauge(isWithGraphics());
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }
}
