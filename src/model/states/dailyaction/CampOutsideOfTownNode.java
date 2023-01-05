package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.states.NoLodgingState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

public class CampOutsideOfTownNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("travel", "world_foreground.png", 0x52,
            MyColors.GREEN, TownSubView.PATH_COLOR, MyColors.DARK_GREEN, MyColors.YELLOW);
    private final boolean freeRations;

    public CampOutsideOfTownNode(boolean freeRations) {
        super("Make camp on the outskirts of town");
        this.freeRations = freeRations;
    }

    @Override
    public GameState getDailyAction(Model model) {
        return new NoLodgingState(model, freeRations);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneDuring(Model model, TownDailyActionState state, int timeOfDay) {
        if (timeOfDay == TownDailyActionState.MORNING) {
            state.println("It's too early to make camp yet!");
            return false;
        }
        return true;
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
