package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.journal.JournalEntry;
import model.states.DailyEventState;
import model.tasks.BountyDestinationTask;
import util.MyRandom;
import util.MyStrings;

import java.util.List;

public class FindBountyEvent extends DailyEventState {
    private final BountyDestinationTask task;

    public FindBountyEvent(Model model, BountyDestinationTask bountyDestinationTask) {
        super(model);
        this.task = bountyDestinationTask;
    }

    @Override
    protected void doEvent(Model model) {
        print("You find " + task.getDestinationDescription() + ". You step inside to find ");
        List<Enemy> enemies = task.makeEnemies(model);
        if (enemies.size() > 1) {
            println(MyStrings.numberWord(enemies.size()) + " people. You assume the person you're looking " +
                    "for is among them.");
        } else {
            println("only one person - " + task.getBountyName() + ".");
        }
        leaderSay(task.getBountyName() + ", your tricks are up! " +
                "You are ordered to stand down and come with us to " + task.getTurnInTown() + ", where you will face justice.");
        showExplicitPortrait(model, task.getBountyPortrait(), task.getBountyName());
        portraitSay(MyRandom.sample(List.of("Another bounty hunter? Good, I was starting to get bored!",
                "You think you can take me?", "You never should have come here.",
                "You're no match for me!", "I'm going nowhere fool.")));
        String fightString = task.getBountyName();
        if (enemies.size() > 1) {
            fightString += "'s gang";
        }
        print("Do you want to fight " + fightString + "? (Y/N) ");
        if (!yesNoInput()) {
            if (enemies.size() > 1 && MyRandom.flipCoin()) {
                println("You quickly turn toward the door, but find that your way is blocked by one of " +
                        task.getBountyName() + "'s cronies.");
                portraitSay("You think we would let you get away? I'm afraid your journey ends here my friend.");
            } else {
                println("You quickly flee out the door.");
                return;
            }
        } else {
            portraitSay("Come on then!");
        }
        runCombat(enemies, task.getCombatTheme(), true);
        if (enemies.get(0).isDead() ) {
            setCurrentTerrainSubview(model);
            task.setBountyKilled();
            JournalEntry.printJournalUpdateMessage(model);
            leaderSay("Well, it did say 'dead or alive'. Now we just need to head back to " +
                    task.getTurnInTown() + " to collect our payment.");
        }
    }
}
