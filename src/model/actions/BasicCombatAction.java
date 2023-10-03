package model.actions;

import model.Model;
import model.combat.CombatAction;
import view.help.HelpDialog;
import view.help.TutorialCombatActionsDialog;

public abstract class BasicCombatAction extends CombatAction {
    public BasicCombatAction(String name, boolean fatigue) {
        super(name, fatigue);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialCombatActionsDialog(model.getView());
    }
}
