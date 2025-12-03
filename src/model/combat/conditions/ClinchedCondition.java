package model.combat.conditions;

import model.Model;
import model.combat.abilities.CombatAction;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.help.HelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.util.List;

public class ClinchedCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD0), MyColors.WHITE, MyColors.BLACK, MyColors.CYAN);
    private static final String TEXT = "A condition indicating that this combatant is clinched with another combatant. " +
            "When a character is clinched, he or she can take no actions except passing or releasing the clinch.";
    private final Condition otherCond;
    private final Combatant clinchedTarget;

    public ClinchedCondition(Combatant clinchedTarget, Condition conditionClinchedWith) {
        super("Clinched", "CLI");
        this.otherCond = conditionClinchedWith;
        this.clinchedTarget = clinchedTarget;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        super.endOfCombatRoundTrigger(model, state, comb);
        if (comb.isDead()) {
            clinchedTarget.removeCondition(otherCond.getClass());
            comb.removeCondition(ClinchedCondition.class);
        }
    }

    // TODO: Override remove at end of combat => true

    @Override
    public void manipulateCombatActions(List<CombatAction> result) {
        result.removeIf((CombatAction ca) -> !(ca.getName().contains("Pass")));
        result.add(0, new CombatAction("Release", false, false) {
            @Override
            public HelpDialog getHelpChapter(Model model) {
                return null; // Unused
            }

            @Override
            protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                combat.println(performer.getName() + " is releasing " + GameState.hisOrHer(performer.getGender()) + " clinch on " + clinchedTarget.getName() + ".");
                clinchedTarget.removeCondition(otherCond.getClass());
                performer.removeCondition(ClinchedCondition.class);
            }
        });
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, TEXT);
    }
}
