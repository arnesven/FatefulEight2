package model.states.duel;

import view.ScreenHandler;
import view.widget.PowerGaugeWidget;

public abstract class PowerGauge {

    private final PowerGaugeWidget widget;
    private final boolean withGraphics;
    private final int[] levelIndices;
    private int currentLevel = 0;

    public PowerGauge(int[] levelIndices, boolean withGraphics) {
        this.levelIndices = levelIndices;
        this.withGraphics = withGraphics;
        if (withGraphics) {
            this.widget = makeWidget();
        } else {
            this.widget = null;
        }
    }

    protected abstract PowerGaugeWidget makeWidget();

    public abstract int getMaxLevel();

    protected abstract int powerRequiredForStrength(int strength);

    public void addToLevel(int i) {
        currentLevel = Math.max(0, Math.min(getMaxLevel(), currentLevel + i));
    }

    public boolean isFull() {
        return currentLevel == getMaxLevel();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void drawYourself(ScreenHandler screenHandler, int xOffset, int yOffset) {
        if (withGraphics) {
            widget.drawYourself(screenHandler, xOffset, yOffset);
        }
    }

    public int drainForSpecialAttack() {
        int strength = getCurrentStrength();
        currentLevel = 0;
        return strength;
    }

    public boolean canDoSpecialAttack() {
        return currentLevel >= powerRequiredForStrength(1);
    }

    public int getCurrentStrength() {
        for (int i = 0; i < levelIndices.length; ++i) {
            if (getCurrentLevel() < powerRequiredForStrength(i + 1)) {
                return i;
            }
        }
        return levelIndices.length;
    }

    public void setCurrentLevel(int i) {
        this.currentLevel = i;
    }

    public int getNoOfSegments() {
        return levelIndices[levelIndices.length-1] + 1;
    }

    public int getLevelIndices(int i) {
        return levelIndices[i];
    }

    public int getLevels() {
        return levelIndices.length;
    }

    public abstract PowerGaugeSegment getSegment(int segment);

    public abstract int getCurrentSegmentIndex();

    public abstract int getPowerPerSegment(int segmentIndex);
}
