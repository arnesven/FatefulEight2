package model.combat.conditions;

import view.GameView;
import view.help.ConditionHelpDialog;
import view.sprites.Sprite;

public class TamedCondition extends Condition {
    private final Sprite sprite;

    public TamedCondition() {
        super("Tamed", "TMD");
        this.sprite = new ParalysisCondition().getSymbol();
    }

    @Override
    protected boolean noCombatTurn() {
        return true;
    }

    @Override
    public Sprite getSymbol() {
        return sprite;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, new TamedCondition(),
                "A condition indicating that the target has been subdued by a taming spell.");
    }
}
