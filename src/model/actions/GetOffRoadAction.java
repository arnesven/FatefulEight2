package model.actions;

import model.Model;
import model.states.DailyActionState;

public class GetOffRoadAction extends DailyAction {
    public GetOffRoadAction(Model model) {
        super("Get Off Road", new DailyActionState(model));
    }

    @Override
    public char getShortKey() {
        return 'F';
    }

    @Override
    public void runPreHook(Model model) {
        model.getParty().setOnRoad(false);
    }
}
