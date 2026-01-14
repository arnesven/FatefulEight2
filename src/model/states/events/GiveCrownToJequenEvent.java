package model.states.events;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.journal.JournalEntry;
import model.mainstory.jungletribe.GainSupportOfJungleTribeTask;
import model.states.DailyEventState;
import model.states.GameState;

public class GiveCrownToJequenEvent extends DailyEventState {
    private final GainSupportOfJungleTribeTask task;

    public GiveCrownToJequenEvent(Model model, GainSupportOfJungleTribeTask gainSupportOfJungleTribeTask) {
        super(model);
        this.task = gainSupportOfJungleTribeTask;
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, task.getJequenPortrait(), "Prince Jequen");
        portraitSay("You have returned. Have you found the Jade Crown?");
        leaderSay("Yes, it was indeed hidden in the " + task.getCrownLocation(model).getName() + ". Here it is.");
        println(model.getParty().getLeader().getName() + " hands Jequen the brilliant crown.");
        portraitSay("Extraordinary! But what about my father? Did you find out what happened to him?");
        if (task.isFriendOfJaquarKnown()) {
            leaderSay("In fact, " + iOrWe() + " did.");
            println(model.getParty().getLeader().getName() + " tells Jequen about the meeting with Jaquar's friend, " +
                    "and their failed expedition to the pyramid.");
            portraitSay("It saddens me to hear that tale, but I'm happy I know the truth and not just legends and rumors.");
        } else {
            leaderSay("No, I'm afraid not. But it seems likely he perished while searching for the crown.");
            portraitSay("Yes. It's what I've always believed.");
        }
        leaderSay("So what will you do now Jequen?");
        portraitSay("I never thought I would ever be in this situation, but now that I'm here, it's clear what I must do. I must " +
                "become the king of the Southern Kingdom.");
        leaderSay("Huzzah!");
        portraitSay("And you shall have the support you wanted. When the come times, I'll meet you at the rendezvous point with " +
                "a force to be reckoned with.");
        leaderSay("That's great. Thanks a lot Jequen!");
        task.setCrownGivenToJequen();
        JournalEntry.printJournalUpdateMessage(model);
    }
}
