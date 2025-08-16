package model.states.dailyaction;

import model.Model;
import model.states.EveningState;
import model.states.GameState;
import model.states.events.InformationBrokerAssassinationEndingEvent;
import model.tasks.AssassinationDestinationTask;
import model.tasks.AssassinationEnding;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.LodgingSummarySubView;
import view.subviews.SubView;
import view.subviews.TavernSubView;

public class LodgingNode extends DailyActionNode {
    public static final Sprite SPRITE = new Sprite32x32("table", "world_foreground.png", 0x54,
            MyColors.DARK_GRAY, TavernSubView.FLOOR_COLOR, MyColors.BROWN);
    private final boolean freeLodging;
    private boolean giveAssassinationEndingEvent = false;

    public LodgingNode(boolean freeLodging) {
        super("Stay at the tavern for the night.");
        this.freeLodging = freeLodging;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (giveAssassinationEndingEvent) {
            return new InformationBrokerAssassinationEndingEvent(model);
        }
        return new LodgingState(model, freeLodging);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (AssassinationDestinationTask.runLodgingNodeHook(model, state)) {
            giveAssassinationEndingEvent = true;
            return true;
        }
        if (state.isMorning()) {
            state.println("It's too early to hit the sack.");
            return false;
        }
        if (GameState.partyIsCreepy(model)) {
            state.println("The bartender refuses to serve you!");
            return false;
        }
        if (!EveningState.partyCanAffordLodging(model)) {
            state.println("You can't afford to pay for food and lodging here.");
            return false;
        }
        SubView prev = model.getSubView();
        model.setSubView(new LodgingSummarySubView(model));
        state.print("Pay " + EveningState.lodgingCost(model) + " for food and lodging here? (Y/N) ");
        boolean toReturn = state.yesNoInput();
        model.setSubView(prev);
        return toReturn;
    }

    @Override
    public boolean exitsCurrentLocale() {
        return false;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        // done by state
    }

    @Override
    public boolean returnNextState() {
        return true;
    }
}
