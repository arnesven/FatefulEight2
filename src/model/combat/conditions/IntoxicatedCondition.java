package model.combat.conditions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.util.ArrayList;

public class IntoxicatedCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xC1), MyColors.GOLD, MyColors.BLACK, MyColors.CYAN);

    public IntoxicatedCondition() {
        super("Intoxicated", "ITX");
    }

    public static void addSkillModifier(GameCharacter gc) {
        gc.addTemporaryBonus(Skill.Logic, -1, true);
        gc.addTemporaryBonus(Skill.Perception, -1, true);
        gc.addTemporaryBonus(Skill.Entertain, 1, true);
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
        return new ConditionHelpDialog(view, this, "A condition indicating that this character is intoxicated. " +
                "An intoxicated character has a -1 modifier to Logic and Perception skill checks, " +
                "but a +1 modifier to Entertain skill checks. Consuming intoxicating beverages while intoxicated may result in " +
                "loss of stamina and/or health.");
    }

    @Override
    public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
        ((GameCharacter) comb).removeTemporaryBonus(Skill.Logic);
        ((GameCharacter) comb).removeTemporaryBonus(Skill.Perception);
        ((GameCharacter) comb).removeTemporaryBonus(Skill.Entertain);
        comb.removeCondition(IntoxicatedCondition.class);
    }
}
