package model.actions;

import model.Model;
import model.states.DailyActionState;

public class EnterCavesAction extends DailyAction {
    public EnterCavesAction(Model model) {
        super("Enter Caves", new DailyActionState(model));
    }

    @Override
    public void runPreHook(Model model) {
        model.enterCaveSystem();
    }
}
