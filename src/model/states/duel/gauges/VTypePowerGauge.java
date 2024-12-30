package model.states.duel.gauges;

import model.states.duel.AIMatrices;
import model.states.duel.AIMatricesPresets;
import view.widget.PowerGaugeWidget;
import view.widget.VGaugeWidget;

public class VTypePowerGauge extends ComplexPowerGauge {

    private static final int[] LEVEL_INDICES = new int[]{9, 17, 25};
    private static final int[] POWER_PER_SEGMENT = new int[]{4, 8, 12};
    private static final PowerGaugeSegment NARROW_SEGMENT_WITH_LINES = new NarrowSegmentWithLines();
    private static final PowerGaugeSegment MEDIUM_SEGMENT_WITH_LINES = new MediumSegmentWithLines();
    private static final PowerGaugeSegment NARROW_SEGMENT = new NarrowSegment();
    private static final PowerGaugeSegment MEDIUM_SEGMENT = new MediumSegment();
    private static final PowerGaugeSegment WIDE_SEGMENT = new WideSegment();
    private static final String HELP_TEXT = "The V-Gauge (or Violent Gauge) is known for being fast up to its first level. " +
            "Reaching level two and three however is somewhat slower than the B-Gauge.";

    public VTypePowerGauge(boolean withGraphics) {
        super("V", LEVEL_INDICES, POWER_PER_SEGMENT, withGraphics);
    }

    @Override
    protected PowerGaugeWidget makeWidget() {
        return new VGaugeWidget(this);
    }

    @Override
    public PowerGaugeSegment getSegment(int segment) {
        if (segment == getLevelIndices(0)) {
            return NARROW_SEGMENT_WITH_LINES;
        }
        if (segment == getLevelIndices(1)) {
            return MEDIUM_SEGMENT_WITH_LINES;
        }
        if (segment < getLevelIndices(0)) {
            return NARROW_SEGMENT;
        }
        if (segment < getLevelIndices(1)) {
            return MEDIUM_SEGMENT;
        }
        return WIDE_SEGMENT;
    }

    @Override
    protected AIMatrices getHighLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel6VGauge();
    }

    @Override
    protected AIMatrices getLowLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel1VGauge();
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }

}
