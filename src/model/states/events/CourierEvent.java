package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;


public class CourierEvent extends DailyEventState {
    private final boolean withIntro;
    private Race race;

    public CourierEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
        this.race = Race.ALL;
    }

    @Override
    public String getDistantDescription() {
        return "a person traveling alone";
    }

    public CourierEvent(Model model) {
        this(model, true);
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    protected void doEvent(Model model) {
        List<UrbanLocation> list = model.getWorld().getLordLocations();
        if (model.getParty().getSummons().size() == list.size()) {
            new NoEventState(model).run(model);
            return;
        }

        UrbanLocation destination;
        do {
            destination = MyRandom.sample(list);
        } while (model.getParty().getSummons().containsKey(destination.getPlaceName()));


        showRandomPortrait(model, Classes.None, this.race, "Courier");
        if (withIntro) {
            println("A courier catches up to you and asks you to stop while " + heOrShe(getPortraitGender()) +
                    " catches " + hisOrHer(getPortraitGender()) + " breath.");
        }
        portraitSay("'" + model.getParty().getLeader().getFullName() + "'s Company' - that's you right? I have a letter for you.");
        println(heOrSheCap(getPortraitGender()) + " hands you a letter which reads: 'You have been summoned by the honorable " + destination.getLordName() + " to " +
                destination.getPlaceName() + ".'");
        println("That's all the letter says. You stare blankly at it and then look at the messenger.");
        leaderSay("Do you know what this is about?");
        portraitSay("Sorry, I'm just a messenger.");
        println(heOrSheCap(getPortraitGender()) + " quickly takes off in the same direction from which " + heOrShe(getPortraitGender()) + " came. " +
                "You put the letter in your pocket and continue on your journey.");
        model.getParty().addSummon(destination);
        JournalEntry.printJournalUpdateMessage(model);
        randomSayIfPersonality(PersonalityTrait.narcissistic, new ArrayList<>(),
                "I'm not surprised that word of my magnificent has reached the people of " + destination.getPlaceName() + ".");
    }
}
