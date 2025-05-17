package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.events.SilentNoEventState;
import model.tasks.DestinationTask;
import model.tasks.MonsterHuntDestinationTask;
import util.MyLists;

import java.util.List;

public class GetPaidForMonstersDailyAction extends GameState {
    private final UrbanLocation location;

    public GetPaidForMonstersDailyAction(Model model, UrbanLocation location) {
        super(model);
        this.location = location;
    }

    @Override
    public GameState run(Model model) {
        lordSay("Yes hello? What is your business here?");

        List<MonsterHuntDestinationTask> dts = MyLists.transform(MyLists.filter(model.getParty().getDestinationTasks(),
                dt -> dt instanceof MonsterHuntDestinationTask &&
                        ((MonsterHuntDestinationTask)dt).canBeTurnedInHere(model)),
                dt -> (MonsterHuntDestinationTask) dt);

        for (MonsterHuntDestinationTask dt : dts) {
            leaderSay(iOrWe() + " are here to collect the reward for slaying the " + dt.getMonster().getName() + ".");
            lordSay("Ah yes, I heard somebody had taken care of it. That was you?");
            leaderSay("Yes, it was " + meOrUs() + ". If you want proof " + iOrWe() + " have a bloody bag of...");
            lordSay("No! I believe you. Let me get my purse. What was the promised sum again?");
            leaderSay("It was " + dt.getReward() + " gold pieces.");
            println("The " + location.getLordTitle() + " counts out " + dt.getReward() + " gold pieces and hands them to you.");
            model.getParty().addToGold(dt.getReward());
            lordSay("Thank you. That " + dt.getMonster().getName() + " has been a real nuisance to us.");
            leaderSay("I'm glad " + iOrWe() + " could be of service.");
            dt.setCompleted(true);
        }
        lordSay("Please enjoy your stay here in " + location.getPlaceName() + ".");
        leaderSay("Thank you. Bye for now.");
        return new SilentNoEventState(model);
    }

    private void lordSay(String line) {
        printQuote(location.getLordName(), line);
    }
}
