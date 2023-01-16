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
    private static final Sprite SPRITE1 = new Sprite32x32("travel", "world_foreground.png", 0x32,
            TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SPRITE2 = new Sprite32x32("travel", "world_foreground.png", 0x32,
            CastleSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private final Sprite sprite;

    public TravelNode(MyColors color) {
        super("Travel");
        if (color == TownSubView.GROUND_COLOR) {
            sprite = SPRITE1;
        } else {
            sprite = SPRITE2;
        }
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
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
