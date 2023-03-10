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
    private static final Sprite SPRITE1 = new Sprite32x32("travel", "world_foreground.png", 0x52,
            TownSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.DARK_GREEN, MyColors.YELLOW);
    private static final Sprite SPRITE2 = new Sprite32x32("travel", "world_foreground.png", 0x52,
            CastleSubView.GROUND_COLOR, TownSubView.PATH_COLOR, MyColors.DARK_GREEN, MyColors.YELLOW);
    private final boolean freeRations;
    private final Sprite sprite;

    public CampOutsideOfTownNode(boolean freeRations, MyColors color) {
        super("Make camp on the outskirts of town");
        this.freeRations = freeRations;
        if (color == TownSubView.GROUND_COLOR) {
            sprite = SPRITE1;
        } else {
            sprite = SPRITE2;
        }
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
