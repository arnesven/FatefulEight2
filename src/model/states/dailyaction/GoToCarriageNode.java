package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.events.TravelByCarriageState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

public class GoToCarriageNode extends DailyActionNode {

    private static Sprite sprite;
    private final TravelByCarriageState travelState;

    public GoToCarriageNode(Model model) {
        super("Go to carriage");
        sprite = new Sprite32x32("carriage", "world_foreground.png", 0xD1,
                model.getTimeOfDay() == TimeOfDay.EVENING ? TownSubView.GROUND_COLOR_NIGHT : TownSubView.GROUND_COLOR,
                TownSubView.PATH_COLOR,
                model.getTimeOfDay() == TimeOfDay.EVENING ? MyColors.BLACK : MyColors.BROWN);
        travelState = new TravelByCarriageState(model);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return travelState;
    }


    @Override
    public boolean returnNextState() {
        return travelState.didTravel();
    }

    @Override
    public Sprite getBackgroundSprite() {
        return sprite;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.isEvening()) {
            state.println("It's too late to leave by carriage today. Come back tomorrow.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        // Done in travelState
    }
}
