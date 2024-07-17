package model.combat.conditions;

import view.GameView;
import view.ScreenHandler;
import view.help.ConditionHelpDialog;
import view.sprites.BatSprite;
import view.sprites.Sprite;

import java.awt.*;

public class EnemyBatFormCondition extends Condition {
    private final BatSprite sprite;
    private final BatFormCondition innerCond;

    public EnemyBatFormCondition() {
        super("Bat Form", "BAT");
        this.sprite = new BatSprite();
        this.innerCond = new BatFormCondition();
    }

    @Override
    protected boolean noCombatTurn() {
        return true;
    }

    @Override
    public int getSpeedBonus() {
        return innerCond.getSpeedBonus();
    }

    @Override
    public boolean hasAlternateAvatar() {
        return true;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos) {
        screenHandler.register(sprite.getName(), new Point(xpos, ypos), sprite);
    }

    @Override
    public Sprite getSymbol() {
        return innerCond.getSymbol();
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return innerCond.getHelpView(view);
    }
}
