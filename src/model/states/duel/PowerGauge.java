package model.states.duel;

import view.ScreenHandler;
import view.widget.PowerGaugeWidget;

public class PowerGauge {

    private final PowerGaugeWidget widget;

    private final int noOfSegments = 24;
    private static final int POWER_PER_SEGMENT = 8;
    private final int[] LEVEL_INDICES = new int[]{7, 15, 23};
    private int currentLevel = 0;

    public PowerGauge() {
        this.widget = new PowerGaugeWidget(this);

    }

    public void addToLevel(int i) {
        currentLevel = Math.max(0, Math.min(maxLevel(), currentLevel + i));
    }

    private int maxLevel() {
        return noOfSegments * POWER_PER_SEGMENT;
    }

    public boolean isFull() {
        return currentLevel == maxLevel();
    }


    public int getNoOfSegments() {
        return noOfSegments;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getLevelIndices(int i) {
        return LEVEL_INDICES[i];
    }

    public int getLevels() {
        return LEVEL_INDICES.length;
    }

    public void drawYourself(ScreenHandler screenHandler, int xOffset, int yOffset) {
        widget.drawYourself(screenHandler, xOffset, yOffset);
    }

    public int drainForSpecialAttack() {
        int strength = getCurrentStrength();
        currentLevel = 0;
        return strength;
    }

    private int powerRequiredForStrength(int i) {
        return (LEVEL_INDICES[i - 1] + 1) * POWER_PER_SEGMENT;
    }

    public boolean canDoSpecialAttack() {
        return currentLevel >= powerRequiredForStrength(1);
    }

    public int getCurrentStrength() {
        for (int i = 0; i < LEVEL_INDICES.length; ++i) {
            if (currentLevel < powerRequiredForStrength(i + 1)) {
                return i;
            }
        }
        return LEVEL_INDICES.length;
    }
}
