package model.states.duel.gauges;

import model.states.duel.AIMatrices;
import model.states.duel.AIMatricesPresets;
import view.widget.CGaugeWidget;
import view.widget.PowerGaugeWidget;

public class CTypePowerGauge extends BTypePowerGauge {

    private static final PowerGaugeSegment LEFT_LEANING_SEGMENT = new LeftLeaningSegment();
    private static final PowerGaugeSegment RIGHT_LEANING_SEGMENT = new RightLeaningSegment(-4);
    private static final PowerGaugeSegment SHIFTED_MEDIUM_SEGMENT = new MediumSegment(-4);
    private static final PowerGaugeSegment SHIFT_MEDIUM_SEGMENT_WITH_LINES = new MediumSegmentWithLines(-4);
    private static final String HELP_TEXT = "The C-Gauge (or Constant Gauge) is a good option for " +
            "duelist who want to launch special attacks more often. The C-Gauge has the same size as a B-Gauge, " +
            "but does not drain fully when a special attack is used. Instead, the C-Gauge reverts down to the lower bound of " +
            "nearest filled level.";

    public CTypePowerGauge(boolean withGraphics) {
        super(withGraphics);
    }

    @Override
    protected PowerGaugeWidget makeWidget() {
        return new CGaugeWidget(this);
    }

    @Override
    public int drainForSpecialAttack() {
        if (getCurrentStrength() <= 1) {
            return super.drainForSpecialAttack();
        }
        int strength = 1;
        setCurrentLevel(powerRequiredForStrength(getCurrentStrength() - 1));
        return strength;
    }

    public void refundPower(int powerPaid) {
        addToLevel(16);
    }

    @Override
    public String getLabelForLevel(int level) {
        return "1";
    }

    @Override
    public String getName() {
        return "C";
    }

    @Override
    public PowerGaugeSegment getSegment(int segment) {
        int lowerBound = getLevelIndices(0) - 1;
        int upperBound = getLevelIndices(1) + 1;
        if (segment == lowerBound) {
            return LEFT_LEANING_SEGMENT;
        }
        if (segment == upperBound) {
            return RIGHT_LEANING_SEGMENT;
        }
        if (segment == getLevelIndices(0) || segment == getLevelIndices(1)) {
            return SHIFT_MEDIUM_SEGMENT_WITH_LINES;
        }
        if (lowerBound <= segment && segment <= upperBound) {
            return SHIFTED_MEDIUM_SEGMENT;
        }
        return super.getSegment(segment);
    }

    @Override
    protected AIMatrices getHighLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel6CGauge();
    }

    @Override
    protected AIMatrices getLowLevelAIMatrices() {
        return AIMatricesPresets.mutatedLevel1CGauge();
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }

    @Override
    public PowerGauge copy() {
        return new CTypePowerGauge(isWithGraphics());
    }
}
