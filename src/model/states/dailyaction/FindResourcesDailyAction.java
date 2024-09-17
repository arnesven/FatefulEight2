package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;

import java.util.List;

public class FindResourcesDailyAction extends DailyAction {
    private static int lastFishedOn = -1;

    private FindResourcesDailyAction(Model model) {
        super("Find Resources", new FindResourcesState(model));
    }

    public static void addActionIfApplicable(Model model, List<DailyAction> actions) {
        if (!alreadyDone(model) && !model.getParty().isOnRoad()) {
            actions.add(new FindResourcesDailyAction(model));
        }
    }

    private static boolean alreadyDone(Model model) {
        return lastFishedOn == model.getDay();
    }

    @Override
    public void runPreHook(Model model) {
        lastFishedOn = model.getDay();
    }
}
