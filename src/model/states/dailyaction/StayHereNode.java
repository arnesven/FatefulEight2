package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.states.StayInHexState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

class StayHereNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("stayhere", "world_foreground.png", 0x12,
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.GRAY, MyColors.BROWN);

    public StayHereNode() {
        super("Stay in town (resolve an event)");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new StayInHexState(model);
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
        townDailyActionState.println("It's too late in the day for that.");
        return false;
    }

    @Override
    public boolean isFreeAction() {
        return false;
    }

}
