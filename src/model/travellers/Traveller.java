package model.travellers;

import model.characters.appearance.AdvancedAppearance;
import model.map.HexLocation;
import model.races.Race;
import model.states.DailyEventState;
import model.states.dailyaction.AdvancedDailyActionState;
import util.MyRandom;

import java.io.Serializable;
import java.util.List;

public class Traveller implements Serializable {

    private final String name;
    private final AdvancedAppearance appearance;
    private final String destination;
    private final int time;
    private final int gold;

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
                " for escorting " + DailyEventState.himOrHer(appearance.getGender()) + ".";
    }

    public String getName() {
        return name;
    }

    public Race getRace() {
        return appearance.getRace();
    }

    public void printReady(AdvancedDailyActionState state) {
        state.printQuote(name, MyRandom.sample(List.of("I'm ready. When are we leaving?",
                "I'm eager to get going.", "Lead the way, adventurer!", "I'm happy to have company on my journey.")));
    }

    public void refuseLowLevel(AdvancedDailyActionState state) {
        state.printQuote(name, "I'm looking for somebody to escort me to " + destination +
                ". You? You don't really look like you're up to the task. No offense.");
    }

    public void refuseNotoriety(AdvancedDailyActionState state) {
        state.printQuote(name, "Me? I'm... uh... just enjoying a brew.");
        state.leaderSay("You aren't you looking for an escort?");
        state.printQuote(name, "Oh, hehehe... what gave you that idea? No no... not me.");
    }
}
