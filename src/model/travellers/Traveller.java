package model.travellers;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.journal.JournalEntry;
import model.map.HexLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import util.MyRandom;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class Traveller implements Serializable {

    private final String name;
    private final AdvancedAppearance appearance;
    private final String destination;
    private final int time;
    private final int gold;
    private int acceptedOnDay = 0;

    public Traveller(String name, AdvancedAppearance appearance, HexLocation destination, int time, int gold) {
        this.name = name;
        this.appearance = appearance;
        this.destination = destination.getName();
        this.time = time;
        this.gold = gold;
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

    public void printReady(GameState state) {
        state.printQuote(name, MyRandom.sample(List.of("I'm ready. When are we leaving?",
                "I'm eager to get going.", "Lead the way, adventurer!", "I'm happy to have company on my journey.")));
    }

    public void refuseLowLevel(GameState state) {
        state.printQuote(name, "I'm looking for somebody to escort me to " + destination +
                ". You? You don't really look like you're up to the task. No offense.");
    }

    public void refuseNotoriety(GameState state) {
        state.printQuote(name, "Me? I'm... uh... just enjoying a brew.");
        state.leaderSay("You aren't you looking for an escort?");
        state.printQuote(name, "Oh, hehehe... what gave you that idea? No no... not me.");
    }


    public void complain(Model model, GameState state) {
        state.println(name + " approaches you.");
        state.printQuote(name, "Hey, are we getting to " + destination + " anytime soon? " +
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
        state.printQuote(name, "Thank you for safely delivering me to my destination.");
        if (getRemainingDays(model) >= 0) {
            state.printQuote(name, "Here's the gold I promised you.");
            state.println("The party receives " + gold + " gold.");
            model.getParty().addToGold(gold);
        } else {
            state.printQuote(name, "I just wish we would have gotten here a little sooner. " +
                    "Now there will be consequences and I'm afraid I won't be able to pay you the full amount I promised.");
            state.println("The party receives " + (gold/2) + " gold.");
            model.getParty().addToGold((gold/2));
        }
        state.println("You part ways with " + name + ".");
        model.getParty().completeTraveller(this);
    }


    public void abandon(Model model, GameState state) {
        state.println(name + " approaches you. " + DailyEventState.heOrSheCap(appearance.getGender()) + " looks annoyed.");
        state.printQuote(name, "I'm fed up with you. I'm going to " + destination + " on my own.");
        state.println(name + " stomps off. You are no longer escorting " + name + ".");
        model.getParty().abandonTraveller(this);
    }

    public JournalEntry getJournalEntry(Model model, boolean active) {
        return new JournalEntry() {
            @Override
            public String getName() {
                return "Escort " + name;
            }

            @Override
            public String getText() {
                return "You have accepted to escort " + name + " to " + destination + " within " + time + " days. " +
                        "You were promised " + gold + " gold.\n\nThere are " + getRemainingDays(model) + " days remaining.";
            }

            @Override
            public boolean isComplete() {
                return !active;
            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                return model.getWorld().getPositionForLocation(getDestinationLocation(model));
            }
        };
    }
}
