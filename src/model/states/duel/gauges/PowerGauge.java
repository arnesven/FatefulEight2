package model.states.duel.gauges;

import model.characters.GameCharacter;
import model.states.duel.AIMatrices;
import model.states.duel.CombinedAIMatrices;
import view.GameView;
import view.ScreenHandler;
import view.help.HelpDialog;
import view.help.PowerGaugeHelpSection;
import view.sprites.AnimationManager;
import view.widget.PowerGaugeWidget;

public abstract class PowerGauge {

    private PowerGaugeWidget widget;
    private final boolean withGraphics;
    private final int[] levelIndices;
    private final String name;
    private int currentLevel = 0;

    public PowerGauge(String name, int[] levelIndices, boolean withGraphics) {
        this.name = name;
        this.levelIndices = levelIndices;
        this.withGraphics = withGraphics;
    }

    @Override
    protected void finalize() throws Throwable {
        AnimationManager.unregister(widget);
    }

    public HelpDialog makeHelpSection(GameView view) {
        return new PowerGaugeHelpSection(view, this);
    }

    protected abstract PowerGaugeWidget makeWidget();
    public abstract int getMaxLevel();
    protected abstract int powerRequiredForStrength(int strength);
    public abstract PowerGaugeSegment getSegment(int segment);
    public abstract int getCurrentSegmentIndex();
    public abstract int getPowerPerSegment(int segmentIndex);
    protected abstract AIMatrices getHighLevelAIMatrices();
    protected abstract AIMatrices getLowLevelAIMatrices();
    public abstract PowerGauge copy();

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
            if (widget == null) {
                widget = makeWidget();
            }
            widget.drawYourself(screenHandler, xOffset, yOffset);
        }
    }

    public void drawGaugeLogo(ScreenHandler screenHandler, int xOffset, int yOffset) {
        if (withGraphics) {
            if (widget == null) {
                widget = makeWidget();
            }
            widget.drawGaugeLogo(screenHandler, xOffset, yOffset);
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

    public String getLabelForLevel(int level) {
        return level + "";
    }

    public void refundPower(int powerPaid) {
        addToLevel(powerPaid / 2);
    }

    public String getName() {
        return name;
    }

    public AIMatrices getAIMatrices(GameCharacter mage) {
        if (mage.getLevel() == 1) {
            return getLowLevelAIMatrices();
        }
        if (mage.getLevel() >= 6) {
            return getHighLevelAIMatrices();
        }
        return new CombinedAIMatrices(mage.getLevel(), getLowLevelAIMatrices(), getHighLevelAIMatrices());
    }

    public abstract String getHelpText();

    public void drawSegments(ScreenHandler screenHandler, int x, int y) {
        if (widget == null) {
            widget = makeWidget();
        }
        widget.drawSegmentsOnly(screenHandler, x, y);
    }

    protected boolean isWithGraphics() {
        return withGraphics;
    }
}
