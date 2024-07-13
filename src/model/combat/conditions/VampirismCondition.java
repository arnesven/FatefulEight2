package model.combat.conditions;

import util.Arithmetics;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.help.VampirismHelpView;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class VampirismCondition extends Condition {

    public static final int NO_STAGE = -1;
    public static final int INITIAL_STAGE = 0;

    private static final Sprite SPRITE = CharSprite.make((char)(0xDC), MyColors.WHITE, MyColors.DARK_RED, MyColors.CYAN);
    private static final int MAX_STAGE = 5;
    private static final int HEALTH_PER_STAGE = 3;
    private static final int STAMINA_PER_STAGE = 2;
    private static final int SPEED_PER_STAGE = 2;
    private static final int CARRY_CAP_BONUS_PER_STAGE = 15;
    private int stage;

    public VampirismCondition(int initialStage) {
        super("Vampirism", "VMP");
        this.stage = initialStage;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new VampirismHelpView(view, this);
    }

    public void progress() {
        this.stage = Arithmetics.incrementWithWrap(this.stage, MAX_STAGE + 1); // TODO: Stop at max instead of wrap-around
    }

    public int getStage() {
        return stage;
    }

    public String getStageString() {
        if (stage == -1) {
            return "";
        }
        return "Current stage: " + stage + "\n\n";
    }

    @Override
    public int getHealthBonus() {
        return HEALTH_PER_STAGE * stage;
    }

    @Override
    public int getStaminaBonus() {
        return STAMINA_PER_STAGE * stage;
    }

    @Override
    public int getSpeedBonus() {
        return SPEED_PER_STAGE * stage;
    }

    @Override
    public int getCarryCapBonus() {
        return CARRY_CAP_BONUS_PER_STAGE * stage;
    }
}
