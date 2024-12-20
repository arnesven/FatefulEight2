package model.combat.conditions;

import model.combat.abilities.CombatAction;
import view.GameView;
import view.MyColors;
import view.ScreenHandler;
import view.help.ConditionHelpDialog;
import view.sprites.BatSprite;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class BatFormCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xD7), MyColors.LIGHT_GRAY, MyColors.BLACK, MyColors.CYAN);
    private static final Sprite AVATAR_SPRITE = new BatSprite();
    private static final int BONUS = 6;
    public static final String DETAILS = "As a bat, your speed is increased by " + BONUS + ", but you can take no actions " +
            "except fleeing and passing.";

    public BatFormCondition() {
        super("Bat Form", "BAT");
        setDuration(BatFormVampireAbility.ROUNDS);
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public int getSpeedBonus() {
        return BONUS;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this character has turned into a bat. " + DETAILS);
    }

    public Sprite getAvatar() {
        return AVATAR_SPRITE;
    }

    @Override
    public void manipulateCombatActions(List<CombatAction> result) {
        result.removeIf((CombatAction ca) -> !(ca.getName().contains("Pass") || ca.getName().contains("Flee")));
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public boolean hasAlternateAvatar() {
        return true;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos) {
        screenHandler.register("batavatar", new Point(xpos, ypos), getAvatar());
    }
}
