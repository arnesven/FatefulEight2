package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.tutorial.TutorialQuickCasting;
import view.help.HelpDialog;

public class QuickCastPassiveCombatAction extends PassiveCombatAction {
    public static final int MINIMUM_RANKS_REQUIRED = 3;

    private static QuickCastPassiveCombatAction instance = null;

    public static QuickCastPassiveCombatAction getInstance() {
        if (instance == null) {
            instance = new QuickCastPassiveCombatAction();
        }
        return instance;
    }

    private QuickCastPassiveCombatAction() {
        super("Quick Casting");
    }

    public static boolean canDoAbility(GameCharacter gc) {
        return gc.getRankForSkill(Skill.SpellCasting) >= MINIMUM_RANKS_REQUIRED;
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialQuickCasting(model.getView());
    }
}
