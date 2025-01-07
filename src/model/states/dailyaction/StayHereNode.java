package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.actions.DailyAction;
import model.actions.StayInHexAction;
import model.states.GameState;
import model.states.StayInHexState;
import model.tasks.DestinationTask;
import util.MyLists;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.ArrowMenuSubView;
import view.subviews.TownSubView;

import java.util.ArrayList;
import java.util.List;

class StayHereNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("stayhere", "world_foreground.png", 0x12,
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.GRAY, MyColors.BROWN);

    public StayHereNode() {
        super("Stay in town (resolve an event)");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        List<DailyAction> actions = new ArrayList<>();
        actions.add(new RandomEventDailyAction(model));
        for (DestinationTask dt : model.getParty().getDestinationTasks()) {
            if (dt.givesDailyAction(model)) {
                actions.add(dt.getDailyAction(model));
            }
        }
        if (actions.size() == 1) {
            return actions.get(0).getState();
        }

        state.print("What event would you like to resolve? ");
        int[] selectedAction = new int[1];
        List<String> optionList = MyLists.transform(actions, DailyAction::getName);
        model.setSubView(new ArrowMenuSubView(model.getSubView(),
                optionList, 30, 18, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selectedAction[0] = cursorPos;
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturn();
        return actions.get(selectedAction[0]).getState();
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState townDailyActionState, Model model) {
        if (townDailyActionState.isEvening()) {
            townDailyActionState.println("It's too late in the day for that.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        model.setTimeOfDay(TimeOfDay.EVENING);
    }

    @Override
    public boolean returnNextState() {
        return true;
    }
}
