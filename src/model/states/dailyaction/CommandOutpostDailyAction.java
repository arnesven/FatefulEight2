package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.map.wars.KingdomWar;
import model.states.events.CommandOutpostDailyEventState;
import model.tasks.BattleDestinationTask;

public class CommandOutpostDailyAction extends DailyAction {
    public CommandOutpostDailyAction(Model model, KingdomWar war, boolean givenByAggressor,
                                     BattleDestinationTask battleDestinationTask) {
        super("Visit Outpost", new CommandOutpostDailyEventState(model, war, givenByAggressor,
                battleDestinationTask));
    }

}
