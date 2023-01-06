package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.states.TravelState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

class TravelNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("travel", "world_foreground.png", 0x32,
            MyColors.GREEN, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);

    public TravelNode() {
        super("Travel");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TravelState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
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
    public boolean exitsTown() {
        return true;
    }

    @Override
    public boolean isFreeAction() {
        return false;
    }
}
