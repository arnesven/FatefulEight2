package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.states.NoLodgingState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CastleSubView;
import view.subviews.TownSubView;

public class CampOutsideOfTownNode extends DailyActionNode {
    private final boolean freeRations;
    private final Sprite sprite;

    public CampOutsideOfTownNode(boolean freeRations, MyColors color, String text) {
        super(text);
        this.freeRations = freeRations;
        sprite = new Sprite32x32("travel", "world_foreground.png", 0x52,
                color, TownSubView.PATH_COLOR, MyColors.DARK_GREEN, MyColors.YELLOW);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new NoLodgingState(model, freeRations);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return sprite;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (!state.isMorning()) {
            return true;
        }
        state.println("It's too early to make camp yet!");
        return false;
    }

    @Override
    public boolean exitsCurrentLocale() {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        // Set by evening event.
    }
}
