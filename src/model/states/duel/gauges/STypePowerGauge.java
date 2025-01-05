package model.states.duel.gauges;

import model.states.duel.AIMatrices;
import model.states.duel.AIMatricesPresets;
import view.widget.PowerGaugeWidget;
import view.widget.SGaugeWidget;

public class STypePowerGauge extends ComplexPowerGauge {

    private static final int[] LEVEL_INDICES = new int[]{5, 11, 17, 23};
    private static final int[] POWER_PER_SEGMENT = new int[]{12, 8, 12, 8};
    
    private static final PowerGaugeSegment WIDE_SEGMENT = new WideSegment();
    private static final PowerGaugeSegment WIDE_SEGMENT_WITH_LINES = new WideSegmentWithLines();
    private static final PowerGaugeSegment RIGHT_LEANING_SEGMENT = new RightLeaningSegment(0);
    private static final PowerGaugeSegment SHIFTED_MEDIUM_SEGMENT = new MediumSegment(4);
    private static final PowerGaugeSegment LEFT_LEANING_SEGMENT_WITH_LINES = new LeftLeaningSegmentWithLines(4);
    private static final String HELP_TEXT = "The S-Gauge (or Super Gauge) is a Power Gauge " +
            "which holds a lot of power, and has four levels instead of the normal three. However, due to its shape it " +
            "is a little slower at reaching the lower levels than most other gauges.";

    public STypePowerGauge(boolean withGraphics) {
        super("S", LEVEL_INDICES, POWER_PER_SEGMENT, withGraphics);
    }

    @Override
    protected PowerGaugeWidget makeWidget() {
        return new SGaugeWidget(this);
    }

    @Override
    public PowerGaugeSegment getSegment(int segment) {
        if (segment < getLevelIndices(0)) {
            return WIDE_SEGMENT;
        }
        if (segment == getLevelIndices(0)) {
            return WIDE_SEGMENT_WITH_LINES;
        }
        if (segment == getLevelIndices(0) + 1) {
            return RIGHT_LEANING_SEGMENT;
        }
        if (segment < getLevelIndices(1)) {
            return SHIFTED_MEDIUM_SEGMENT;
        }
        if (segment == getLevelIndices(1)) {
            return LEFT_LEANING_SEGMENT_WITH_LINES;
        }
        if (segment < getLevelIndices(2)) {
            return WIDE_SEGMENT;
        }
        if (segment == getLevelIndices(2)) {
            return WIDE_SEGMENT_WITH_LINES;
        }
        if (segment == getLevelIndices(2) + 1) {
            return RIGHT_LEANING_SEGMENT;
        }
        return SHIFTED_MEDIUM_SEGMENT;
    }

    @Override
    protected AIMatrices getHighLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel6SGauge();
    }

    @Override
    protected AIMatrices getLowLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel1SGauge();
    }

    @Override
    public PowerGauge copy() {
        return new STypePowerGauge(isWithGraphics());
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }
}
