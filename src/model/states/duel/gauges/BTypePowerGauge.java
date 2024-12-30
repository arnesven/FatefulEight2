package model.states.duel.gauges;

import model.states.duel.AIMatrices;
import model.states.duel.AIMatricesPresets;
import util.MyStrings;
import view.widget.BGaugeWidget;
import view.widget.PowerGaugeWidget;

public class BTypePowerGauge extends PowerGauge {

    private static final PowerGaugeSegment SEGMENT_WITH_LINES = new MediumSegmentWithLines();
    private static final PowerGaugeSegment SEGMENT = new MediumSegment();

    private static final int POWER_PER_SEGMENT = 8;
    private static final int[] LEVEL_INDICES = new int[]{7, 15, 23};
    private static final String HELP_TEXT = "The B-Gauge is the most basic Power Gauge. It has " +
            MyStrings.numberWord(LEVEL_INDICES.length) + " levels, which all store " +
            "the same amount of power.";
    private final int[] requiredStrengths;

    public BTypePowerGauge(boolean withGraphics) {
        super("B", LEVEL_INDICES, withGraphics);

        requiredStrengths = new int[LEVEL_INDICES.length];
        for (int i = 0; i < requiredStrengths.length; ++i) {
            requiredStrengths[i] = (LEVEL_INDICES[i] + 1) * POWER_PER_SEGMENT;
        }
    }

    public BTypePowerGauge() {
        this(true);
    }

    public String getHelpText() {
        return HELP_TEXT;
    }

    @Override
    protected PowerGaugeWidget makeWidget() {
        return new BGaugeWidget(this);
    }

    @Override
    public int getMaxLevel() {
        return requiredStrengths[LEVEL_INDICES.length-1];
    }

    @Override
    protected int powerRequiredForStrength(int strength) {
        return requiredStrengths[strength-1];
    }

    @Override
    public PowerGaugeSegment getSegment(int segment) {
        if (segment == getLevelIndices(0) || segment == getLevelIndices(1)) {
            return SEGMENT_WITH_LINES;
        }
        return SEGMENT;
    }

    @Override
    public int getCurrentSegmentIndex() {
        return getCurrentLevel() / 8;
    }

    @Override
    public int getPowerPerSegment(int segmentIndex) {
        return POWER_PER_SEGMENT;
    }

    @Override
    protected AIMatrices getHighLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel6BGauge();
    }

    @Override
    protected AIMatrices getLowLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel1BGauge();
    }
}
