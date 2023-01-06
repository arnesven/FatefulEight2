package model.states.dailyaction;

import model.Model;
import model.states.EveningState;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TavernSubView;

public class LodgingNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("table", "world_foreground.png", 0x54,
            MyColors.DARK_GRAY, TavernSubView.FLOOR_COLOR, MyColors.BROWN);
    private final boolean freeLodging;

    public LodgingNode(boolean freeLodging) {
        super("Stay at the tavern for the night.");
        this.freeLodging = freeLodging;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new LodgingState(model, freeLodging);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.isMorning()) {
            state.println("It's too early to hit the sack.");
            return false;
        }
        if (!EveningState.partyCanAffordLodging(model)) {
            state.println("You can't afford to pay for food and lodging here.");
            return false;
        }
        return true;
    }

    @Override
    public boolean isFreeAction() {
        return true;
    }

    @Override
    public boolean exitsTown() {
        return false;
    }

    @Override
    public boolean returnNextState() {
        return true;
    }
}
