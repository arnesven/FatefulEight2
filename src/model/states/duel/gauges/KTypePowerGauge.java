package model.states.duel.gauges;

import model.states.duel.AIMatrices;
import model.states.duel.AIMatricesPresets;
import util.MyRandom;
import view.widget.KGaugeWidget;
import view.widget.PowerGaugeWidget;

public class KTypePowerGauge extends PowerGauge {

    private static final int[] LEVEL_INDICES = new int[]{8, 23};
    private static final int POWER_PER_SEGMENT = 8;
    private static final PowerGaugeSegment NORMAL_SEGMENT = new MediumSegment();
    private static final PowerGaugeSegment RIGHT_LEANING_SEGMENT_WITH_LINES = new RightLeaningSegmentWithLines();
    private static final PowerGaugeSegment SHIFTED_NORMAL_SEGMENT = new MediumSegment(4);
    private static final String HELP_TEXT = "The K-Gauge (sometimes known as the Kill Gauge) is " +
            "peculiar since it only has two levels. The second level however does not necessarily provide " +
            "the duelist with a 2-level attack. Instead, an attack with a strength of " +
            "anything from one to five may be generated!";

    private final int[] requiredStrengths;

    public KTypePowerGauge(boolean withGraphics) {
        super("K", LEVEL_INDICES, withGraphics);
        requiredStrengths = new int[LEVEL_INDICES.length];
        for (int i = 0; i < requiredStrengths.length; ++i) {
            requiredStrengths[i] = (LEVEL_INDICES[i] + 1) * POWER_PER_SEGMENT;
        }
    }

    @Override
    protected PowerGaugeWidget makeWidget() {
        return new KGaugeWidget(this);
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
        if (segment < getLevelIndices(0)) {
            return NORMAL_SEGMENT;
        }
        if (segment == getLevelIndices(0)) {
            return RIGHT_LEANING_SEGMENT_WITH_LINES;
        }
        return SHIFTED_NORMAL_SEGMENT;
    }

    @Override
    public String getLabelForLevel(int level) {
        if (level == 2) {
            return "?";
        }
        return super.getLabelForLevel(level);
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }

    @Override
    public int drainForSpecialAttack() {
        super.drainForSpecialAttack();
        return MyRandom.randInt(1, 5);
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
        return AIMatricesPresets.mutatedLevel6KGauge();
    }

    @Override
    protected AIMatrices getLowLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel1KGauge();
    }

    @Override
    public PowerGauge copy() {
        return new KTypePowerGauge(isWithGraphics());
    }
}
