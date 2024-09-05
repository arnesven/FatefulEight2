package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.states.events.FishingState;

import java.util.List;

public class FishingDailyAction extends DailyAction {
    private static int lastFishedOn = 0;

    private FishingDailyAction(Model model) {
        super("Go Fishing", new FishingState(model));
    }

    private static boolean alreadyDone(Model model) {
        return lastFishedOn == model.getDay();
    }

    public static void addActionIfApplicable(Model model, List<DailyAction> actions) {
        if (!model.getParty().isOnRoad() && model.getCurrentHex().getRivers() != 0 && !alreadyDone(model)) {
            actions.add(new FishingDailyAction(model));
        }
    }

    @Override
    public void runPreHook(Model model) {
        lastFishedOn = model.getDay();
    }
}
