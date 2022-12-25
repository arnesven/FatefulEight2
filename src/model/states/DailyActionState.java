package model.states;

import model.Model;
import model.actions.DailyAction;
import view.subviews.OnTheRoadSubView;

import java.util.List;

public class DailyActionState extends GameState {

    public DailyActionState(Model model) {
        super(model);
    }

    public GameState run(Model model) {
        if (!model.getCurrentHex().hasLodging() && model.getParty().isOnRoad()) {
            model.setSubView(OnTheRoadSubView.instance);
        } else {
            model.setSubView(model.getCurrentHex().getImageSubView());
        }
        String place = model.getCurrentHex().getPlaceName();
        print("You are " + place + ". Please select your daily action.\n");
        DailyAction selectedAction = selectDailyAction();
        selectedAction.runPreHook(model);
        return selectedAction.getState();
    }

    private DailyAction selectDailyAction() {
        DailyAction selectedAction = null;
        String dailyActionString = printDailyActions();
        do {
            print(dailyActionString);
            char input = lineInput().charAt(0);
            char lower = ("" + input).toUpperCase().charAt(0);

            for (DailyAction da : getDailyActions()) {
                if (da.getShortKey() == input || da.getShortKey() == lower) {
                    selectedAction = da;
                }
            }
            if (selectedAction == null) {
                println("That is not a valid input.");
            } else {
                break;
            }
        } while (true);
        return selectedAction;
    }

    private String printDailyActions() {
        List<DailyAction> actions = getDailyActions();
        StringBuilder bldr = new StringBuilder();
        for (DailyAction da : actions) {
            bldr.append(da.getName());
            bldr.append(" (");
            bldr.append(da.getShortKey());
            bldr.append("), ");
        }
        bldr.append(": ");
        return bldr.substring(0, bldr.length()-2);
    }
}
