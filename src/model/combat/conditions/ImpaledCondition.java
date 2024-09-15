package model.combat.conditions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.GameState;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class ImpaledCondition extends Condition {
    private static final String TEXT = "A condition indicating that this combatant has been impaled by another combatant. " +
            "An impaled combatant cannot attack any other combatant than the one who impaled it. The condition normally lasts" +
            " until the impaled combatants next turn.";
    private final GameCharacter impaler;

    private static final Sprite SPRITE = CharSprite.make((char) (0xC2), MyColors.BLACK, MyColors.ORANGE, MyColors.CYAN);

    public ImpaledCondition(GameCharacter performer) {
        super("Impaled", "IMP");
        this.impaler = performer;
        setDuration(MyRandom.randInt(2, 5));
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public void wasRemoved(Combatant combatant) {
        super.wasRemoved(combatant);
        if (combatant.hasCondition(WeakenCondition.class)) {
            combatant.removeCondition(WeakenCondition.class);
            combatant.removeCondition(ExposedCondition.class);
            impaler.removeCondition(ClinchedCondition.class);
        }
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        super.endOfCombatRoundTrigger(model, state, comb);
        if (comb.isDead()) {
            state.println(impaler.getFirstName() + " is no longer impaling " + comb.getName() + ".");
            comb.removeCondition(ImpaledCondition.class);
        }
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ImpaledConditionHelpChapter(view);
    }

    public GameCharacter getImapler() {
        return impaler;
    }

    private class ImpaledConditionHelpChapter extends ConditionHelpDialog {
        public ImpaledConditionHelpChapter(GameView view) {
            super(view, ImpaledCondition.this, TEXT);
        }
    }
}
