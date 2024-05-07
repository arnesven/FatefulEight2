package model.actions;

import model.Model;
import model.states.DailyActionState;

public class ExitCavesAction extends DailyAction {
    public ExitCavesAction(Model model) {
        super("Exit Caves", new DailyActionState(model));
    }

    @Override
    public void runPreHook(Model model) {
        model.exitCaveSystem(false);
    }
}
