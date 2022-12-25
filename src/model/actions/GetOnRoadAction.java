package model.actions;

import model.Model;
import model.states.DailyActionState;

public class GetOnRoadAction extends DailyAction {
    public GetOnRoadAction(Model model) {
        super("Get On Road", new DailyActionState(model));
    }

    @Override
    public char getShortKey() {
        return 'R';
    }

    @Override
    public void runPreHook(Model model) {
        model.getParty().setOnRoad(true);
    }
}
