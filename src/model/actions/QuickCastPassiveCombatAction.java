package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.abilities.SkillAbilityCombatAction;
import view.help.TutorialQuickCasting;
import view.help.HelpDialog;

import java.util.List;

public class QuickCastPassiveCombatAction extends PassiveCombatAction implements SkillAbilityCombatAction {
    public static final int MINIMUM_RANKS_REQUIRED = 4;

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

    public boolean canDoAbility(GameCharacter gc) {
        return hasRequiredRanks(gc);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialQuickCasting(model.getView());
    }

    @Override
    public List<Skill> getLinkedSkills() {
        return List.of(Skill.SpellCasting);
    }

    @Override
    public int getRequiredRanks() {
        return MINIMUM_RANKS_REQUIRED;
    }
}
