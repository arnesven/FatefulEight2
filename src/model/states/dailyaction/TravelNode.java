package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.states.TravelState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CastleSubView;
import view.subviews.TownSubView;

class TravelNode extends DailyActionNode {
    private final Sprite sprite;

    public TravelNode(MyColors color) {
        super("Travel");
        sprite = new Sprite32x32("travel", "world_foreground.png", 0x32,
                color, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        model.getParty().setOnRoad(true);
        return new TravelState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return sprite;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState townDailyActionState, Model model) {
        if (townDailyActionState.isMorning()) {
            return true;
        }
        townDailyActionState.println("It's too late in the day to travel. You will have to wait until tomorrow.");
        return false;
    }

    @Override
    public boolean exitsCurrentLocale() {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        // Done by state
    }

}
