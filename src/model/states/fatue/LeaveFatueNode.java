package model.states.fatue;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.NoLodgingState;
import model.states.dailyaction.AdvancedDailyActionState;

class LeaveFatueNode extends FatueDailyActionNode {
    public LeaveFatueNode() {
        super("Leave Fortress at the Ultimate Edge");
    }

    @Override
    public boolean exitsCurrentLocale() {
        return true;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new NoLodgingState(model, false);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        model.setTimeOfDay(TimeOfDay.EVENING);
    }
}
