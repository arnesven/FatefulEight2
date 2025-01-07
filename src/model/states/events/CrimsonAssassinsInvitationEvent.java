package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.tasks.AssassinationDestinationTask;
import model.tasks.WritOfExecution;
import util.MyRandom;
import util.MyStrings;
import view.subviews.PortraitSubView;

import java.util.List;

public class CrimsonAssassinsInvitationEvent extends DailyEventState {
    private static final String ALREADY_DONE_KEY = "CRIMSON_ASSASSINS_INVITATION_GOTTEN";

    public CrimsonAssassinsInvitationEvent(Model model) {
        super(model);
    }

    public static DailyEventState eventDependentOnMurders(Model model) {
        boolean condition =
                GameStatistics.getMurders() > 0 &&
                !alreadyDoneEvent(model) &&
                (model.getParty().isOnRoad() || model.getCurrentHex().getLocation() instanceof UrbanLocation) &&
                MyRandom.rollD6() == 6;

        if (condition) {
            return new CrimsonAssassinsInvitationEvent(model);
        }
        return null;
    }

    public static boolean alreadyDoneEvent(Model model) {
        return model.getSettings().getMiscFlags().containsKey(ALREADY_DONE_KEY);
    }

    @Override
    protected void doEvent(Model model) {
        model.getSettings().getMiscFlags().put(ALREADY_DONE_KEY, true);
        CharacterAppearance app = PortraitSubView.makeRandomPortrait(Classes.ASN);
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
            println("A hooded " + manOrWoman(app.getGender()) + " approaches you on the street.");
        } else {
            println("A hooded " + manOrWoman(app.getGender()) + " catches up to you on the road.");
        }
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, app, "Stranger");
        portraitSay("Greetings killer.");
        leaderSay("Excuse me?");
        portraitSay("You heard me.");
        leaderSay("I think you have me confused with somebody else.");
        portraitSay(model.getParty().getLeader().getName() + (model.getParty().size() > 1 ? " and Company" :"") +
                ", right? No I know exactly who you are, " +
                "and I have a proposition for you.");
        leaderSay("Well, spit it out then.");
        portraitSay("I represent a secret organization. We want to offer you a job.");
        println("The stranger hands you a roll of paper, tied with a crimson ribbon.");
        leaderSay("A job?");
        println("You unroll the paper and read it.");
        leaderSay("This is just a name, and a place. Who is this?");
        portraitSay("Nobody. Just somebody we would like you to kill.");
        leaderSay("Whoa... What kind of organization do you work for?");
        portraitSay("We're called the Crimson Assassins. We are loosely affiliated with " +
                "the Brotherhood, perhaps you've heard of them?");
        leaderSay("Maybe I have, maybe I haven't.");
        portraitSay("We get contracts to carry out assassinations on individuals. We've heard you're " +
                "no stranger to killing. If you accept the writ in your hand, I'll pay you " +
                WritOfExecution.getPayment() + " gold right now. " +
                "All you have to do is go to the location, and kill the person.");
        leaderSay("I assume there is some kind of deadline associated with this? Otherwise, I may take the gold and " +
                "postpone the killing, you know, indefinitely.");
        String deadlineDays = MyStrings.numberWord(WritOfExecution.getDeadlineInDays());
        portraitSay("Of course. We always allow a contractor a " + deadlineDays + " days to carry out the assassination. After that " +
                "we'll assume you have been diverted, detained, killed or otherwise engaged. In that case we will transfer " +
                "your debt to us to the Brotherhood, who usually handles such matters with great efficiency.");
        leaderSay("I think I understand.");
        portraitSay("Good. And if things go well, you can acquire more writs from Brotherhood agents " +
                "in towns and castles. So what do you say, want to join our ranks as an agent of the night?");
        List<GameCharacter> leader = List.of(model.getParty().getLeader());
        boolean said = randomSayIfPersonality(PersonalityTrait.lawful, leader,
                "There is just no way this is legal.");
        if (!said) {
            said = randomSayIfPersonality(PersonalityTrait.benevolent, leader,
                    "This is wrong, plain and simple.");
        }
        if (!said) {
            randomSayIfPersonality(PersonalityTrait.cold, leader,
                    "I say we take the money. So what if we have to off somebody? We've done that before.");
        }
        print("Do you accept the writ? (Y/N) "); // TODO: Add tutorial chapter
        if (yesNoInput()) {
            leaderSay("Yes. Give me the gold.");
            println("The stranger hands you " + WritOfExecution.getPayment() + " gold.");
            model.getParty().addToGold(WritOfExecution.getPayment());
            portraitSay("Excellent. Here you go. Remember, you have " + deadlineDays + " days to carry out the order.");
            leaderSay("Don't worry. I'll get it done in time.");
            portraitSay("I'm not worried. Actually I don't care one way or the other. I'm just a messenger.");
            leaderSay("Okay. See you around.");
            portraitSay("No you won't.");
            JournalEntry.printJournalUpdateMessage(model);
            model.getParty().addDestinationTask(new AssassinationDestinationTask(new WritOfExecution(model)));
        } else {
            leaderSay("No, I can't do this. It just doesn't sit well with me.");
            portraitSay("Well, if you change your mind, you can always talk to a Brotherhood agent in a town or castle.");
            leaderSay("I don't think I will. And, don't take this the wrong way, but I hope I never see you again.");
            portraitSay("I know what your hinting at friend, but regardless, you never will.");
        }
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println("The stranger walks off.");
        randomSayIfPersonality(PersonalityTrait.anxious, leader, heOrSheCap(app.getGender()) + " was creepy.");
    }
}
