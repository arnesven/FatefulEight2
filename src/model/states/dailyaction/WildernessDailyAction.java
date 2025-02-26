package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.states.GameState;
import model.states.SpyglassState;
import model.states.events.FishingState;
import java.util.List;

public class WildernessDailyAction extends DailyAction {

    private static int lastUsedOn = -1;

    private WildernessDailyAction(String name, GameState state) {
        super(name, state);
    }

    public static void addActionsIfApplicable(Model model, List<DailyAction> actions) {
        if (notDoneYetToday(model)) {
            if (!model.getParty().isOnRoad()) {
                actions.add(new WildernessDailyAction("Find Resources", new FindResourcesState(model)));
                if (model.getCurrentHex().getRivers() != 0 && FishingState.countFishingPoles(model) > 0) {
                    actions.add(new WildernessDailyAction("Go Fishing", new FishingState(model)));
                }
            }
            if (model.getCurrentHex().isHighGround() && SpyglassState.hasSpyglass(model)) {
                actions.add(new WildernessDailyAction("Use Spyglass", new SpyglassState(model)));
            }
        }
    }

    private static boolean notDoneYetToday(Model model) {
        return lastUsedOn != model.getDay();
    }

    @Override
    public void runPreHook(Model model) {
        lastUsedOn = model.getDay();
    }
}
