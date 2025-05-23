package model.travellers;

import model.Model;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.journal.JournalEntry;
import model.map.HexLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyRandom;
import view.subviews.SubView;
import view.subviews.TavernSubView;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class Traveller implements Serializable {

    private final String name;
    private final CharacterAppearance appearance;
    private final String destination;
    private final int time;
    private final int gold;
    private final TravellerCompletionHook completionHook;
    private int acceptedOnDay = 0;

    public Traveller(String name, CharacterAppearance appearance, HexLocation destination,
                     int distance, int extraReward, TravellerCompletionHook hook) {
        this.name = name;
        this.appearance = appearance;
        this.destination = destination.getName();
        this.time = MyRandom.randInt(distance-1, (int)(distance*1.5));
        this.gold = MyRandom.randInt(distance/2, distance*2) +
                MyRandom.randInt(distance/2, distance*2) + extraReward;
        this.completionHook = hook;
    }

    public String getAcceptString() {
        return "This " + appearance.getRace().getName() + " traveller wants to go to " + destination + " and would prefer to arrive " +
                "within " + time + " days. " +
                DailyEventState.heOrSheCap(appearance.getGender()) + " is offering " + gold +
                " gold for escorting " + DailyEventState.himOrHer(appearance.getGender()) + ".";
    }

    public String getName() {
        return name;
    }

    public Race getRace() {
        return appearance.getRace();
    }

    public void printReady(Model model, GameState state) {
        travellerSay(model, state,   MyRandom.sample(List.of("I'm ready. When are we leaving?",
                "I'm eager to get going.", "Lead the way, adventurer!", "I'm happy to have company on my journey.")));
    }

    public void refuseLowLevel(Model model, GameState state) {
        travellerSay(model, state,   "I'm looking for somebody to escort me to " + destination +
                ". You? You don't really look like you're up to the task. No offense.");
    }

    public void refuseNotoriety(Model model, GameState state) {
        travellerSay(model, state,   "Me? I'm... uh... just enjoying a brew.");
        state.leaderSay("You aren't you looking for an escort?");
        travellerSay(model, state,   "Oh, hehehe... what gave you that idea? No no... not me.");
    }


    public void complain(Model model, GameState state) {
        state.println(name + " approaches you.");
        travellerSay(model, state,   "Hey, are we getting to " + destination + " anytime soon? " +
                "I have some engagements I don't want to be late for.");
        state.leaderSay("Don't worry. We'll get there soon.");
    }

    public int getRemainingDays(Model model) {
        return time - (model.getDay() - acceptedOnDay);
    }

    public void accept(int day) {
        this.acceptedOnDay = day;
    }

    public HexLocation getDestinationLocation(Model model) {
        return model.getWorld().getLocationByName(destination);
    }

    public void complete(Model model, GameState state) {
        state.println(name + " approaches you.");
        travellerSay(model, state,   "Thank you for safely delivering me to my destination.");
        if (getRemainingDays(model) >= 0) {
            travellerSay(model, state,   "Here's the gold I promised you.");
            state.println("The party receives " + gold + " gold.");
            model.getParty().addToGold(gold);
        } else {
            travellerSay(model, state,   "I just wish we would have gotten here a little sooner. " +
                    "Now there will be consequences and I'm afraid I won't be able to pay you the full amount I promised.");
            state.println("The party receives " + (gold/2) + " gold.");
            model.getParty().addToGold((gold/2));
        }
        completionHook.run(model, state, this);

        if (model.getParty().getLeader().hasPersonality(PersonalityTrait.rude)) {
            state.leaderSay("Smell you later traveller!");
        } else {
            state.leaderSay("It was nice travelling with you.");
        }
        state.println("You part ways with " + name + ".");
        model.getParty().completeTraveller(this);
        JournalEntry.printJournalUpdateMessage(model);
    }

    public void abandon(Model model, GameState state) {
        state.println(name + " approaches you. " + DailyEventState.heOrSheCap(appearance.getGender()) + " looks annoyed.");
        travellerSay(model, state,   "I'm fed up with you. I'm going to " + destination + " on my own.");
        state.println(name + " stomps off. You are no longer escorting " + name + ".");
        model.getParty().abandonTraveller(this);
        model.getParty().addToReputation(-1);
        state.printAlert("Your reputation has decreased.");
        JournalEntry.printJournalUpdateMessage(model);
    }

    public void travellerSay(Model model, GameState state, String text) {
        SubView view = model.getSubView();
        if (view instanceof TavernSubView) {
            ((TavernSubView)view).addCalloutAtTraveller(text.length());
        }
        state.printQuote(name, text);
    }

    public JournalEntry getJournalEntry(Model model, boolean active, boolean completed) {
        return new TravellerJournalEntry(model, active, completed);
    }

    private class TravellerJournalEntry implements JournalEntry {

        private final String text;
        private final boolean complete;
        private final boolean failed;

        public TravellerJournalEntry(Model model, boolean active, boolean completed) {
            if (active) {
                this.text =  "You have accepted to escort " + name + " to " + destination + " within " + time + " days. " +
                        "You were promised " + gold + " gold.\n\nThere are " + getRemainingDays(model) + " days remaining.";
            } else if (completed) {
                this.text = "You escorted " + name + " to " + destination + ".\n\nCompleted";
            } else { // Abandon
                this.text = "You failed to escort " + name + " to " + destination + ".";
            }
            this.complete = !active && completed;
            this.failed = !active && !completed;
        }

        @Override
        public String getName() {
            return "Escort " + name;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public boolean isComplete() {
            return complete;
        }

        @Override
        public boolean isFailed() {
            return failed;
        }

        @Override
        public boolean isTask() {
            return true;
        }

        @Override
        public Point getPosition(Model model) {
            return model.getWorld().getPositionForLocation(getDestinationLocation(model));
        }
    }
}
