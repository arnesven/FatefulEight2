package model.actions;

import model.Model;
import model.combat.abilities.CombatAction;
import view.help.HelpDialog;
import view.help.TutorialCombatActionsDialog;

public abstract class BasicCombatAction extends CombatAction {
    public BasicCombatAction(String name, boolean fatigue, boolean isMeleeAttack) {
        super(name, fatigue, isMeleeAttack);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialCombatActionsDialog(model.getView());
    }
}
