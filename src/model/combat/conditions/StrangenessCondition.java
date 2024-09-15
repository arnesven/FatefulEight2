package model.combat.conditions;

import model.Model;
import model.classes.Skill;
import model.combat.Combatant;
import model.states.GameState;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class StrangenessCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD5), MyColors.GREEN, MyColors.PURPLE, MyColors.CYAN);
    private static final int PENALTY = 2;
    private static final int DURATION_DAYS = 3;
    private final int gottenOnDay;

    public StrangenessCondition(int day) {
        super("Strangeness", "STN");
        this.gottenOnDay = day;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
        if (model.getDay() == gottenOnDay + DURATION_DAYS) {
            comb.removeCondition(StrangenessCondition.class);
        }
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        if (skill == Skill.SeekInfo || skill == Skill.Persuade ||
                skill == Skill.Entertain || skill == Skill.Leadership) {
            return -2;
        }
        return super.getBonusForSkill(skill);
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this character has a strangeness, " +
                        "impairing his or her charisma for " + MyStrings.numberWord(DURATION_DAYS) + " days. " +
                        "Entertain, Leadership, Persuade and Seek Info Skills suffer a -" + PENALTY + " penalty.");
    }
}
