package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.combat.conditions.Condition;
import model.states.CombatEvent;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.help.HelpDialog;
import view.help.TutorialDefending;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class DefendCombatAction extends SpecialAbilityCombatAction {
    public static final int DEFEND_SKILL_RANKS = 3;
    private static final Skill[] SKILLS = new Skill[]{Skill.Axes, Skill.Blades, Skill.BluntWeapons, Skill.Polearms};

    public DefendCombatAction() {
        super("Defend", false, false);
    }

    public static boolean isDefending(GameCharacter gameCharacter) {
        return gameCharacter.hasCondition(DefendCondition.class);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialDefending(model.getView());
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().defending(model);
        combat.println(performer.getFirstName() + " takes a defensive stance.");
        performer.addCondition(new DefendCondition());
    }

    private static final Sprite SPRITE = CharSprite.make((char) (0xD1), MyColors.WHITE, MyColors.BLUE, MyColors.CYAN);

    public Condition getCondition() {
        return new DefendCondition();
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        for (Skill s : SKILLS) {
            if (performer.getUnmodifiedRankForSkill(s) >= DEFEND_SKILL_RANKS) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        for (Skill s : SKILLS) {
            if (performer.getUnmodifiedRankForSkill(s) >= DEFEND_SKILL_RANKS &&
                    performer.getEquipment().getWeapon().getSkill() == s) {
                return true;
            }
        }
        return false;
    }

    private static class DefendCondition extends Condition {
        public DefendCondition() {
            super("Defend", "DEF");
            setDuration(2);
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
        public boolean removeAtEndOfCombat() {
            return true;
        }

        @Override
        public ConditionHelpDialog getHelpView(GameView view) {
            return new ConditionHelpDialog(view, this,
                    "A condition indicating that this combatant is currently in a defensive stance.");
        }
    }
}
