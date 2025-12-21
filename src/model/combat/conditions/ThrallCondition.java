package model.combat.conditions;

import model.characters.GameCharacter;
import model.items.spells.EnthrallSpell;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class ThrallCondition extends Condition {

    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD3), MyColors.DARK_RED, MyColors.BLACK, MyColors.CYAN);
    private final GameCharacter vampire;

    public ThrallCondition(GameCharacter caster) {
        super("Thrall", "THR");
        this.vampire = caster;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "The Thrall condition indicates that this character has been enthralled to a vampire" +
                        (vampire != null ? " (" + vampire.getName() + ")." : "."));
    }

    public static boolean isThrallTo(GameCharacter subject, GameCharacter vampire) {
        return EnthrallSpell.isThrall(subject) && ((ThrallCondition)subject.getCondition(ThrallCondition.class)).vampire == vampire;
    }
}
