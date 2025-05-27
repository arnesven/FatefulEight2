package model.states.dailyaction.town;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.events.TravelByCarriageState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

public class GoToCarriageNode extends DailyActionNode {

    private static Sprite DAY_SPRITE = new Sprite32x32("carriage", "world_foreground.png", 0xD1,
                                                      TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN);
    private static Sprite NIGHT_SPRITE = new Sprite32x32("carriage", "world_foreground.png", 0xD1,
            TownSubView.GROUND_COLOR_NIGHT, TownSubView.PATH_COLOR, MyColors.BLACK);
    private final TravelByCarriageState travelState;
    private final Model model;

    public GoToCarriageNode(Model model) {
        super("Go to carriage");
        this.model = model;
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
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            return NIGHT_SPRITE;
        }
        return DAY_SPRITE;
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
