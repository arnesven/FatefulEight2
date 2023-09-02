package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import model.states.horserace.HorseRaceTrack;
import view.subviews.PortraitSubView;

public class TimedHorseRaceEvent extends DailyEventState {
    public TimedHorseRaceEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("On the outskirts of town, a horse track has been prepared for racing. " +
                "The caretaker is languishing nearby.");
        CharacterAppearance caretaker = PortraitSubView.makeRandomPortrait(Classes.None, Race.ALL);
        showExplicitPortrait(model, caretaker, "Caretaker");
        portraitSay("Care to try out the new track?");
        if (model.getParty().getHorseHandler().isEmpty()) {
            leaderSay("Unfortunately, I don't have a horse.");
            portraitSay("That's to bad, racing's the best there is.");
            println("You leave the horse track.");
            return;
        }
        print("Do you want to race? (Y/N) ");
        if (!yesNoInput()) {
            println("You leave the horse track.");
            return;
        }
        portraitSay("Alright then! The record time is " + model.getParty().getHorseHandler().getTimedRaceRecord() +
                " seconds. If you can beat that, I'll give you 20 gold.");
        GameCharacter chosen = model.getParty().getPartyMember(0);
        if (model.getParty().size() > 1) {
            print("Which party member should ride in the race?");
            chosen = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        }
        HorseRacingEvent horseRace = new HorseRacingEvent(model, chosen, model.getParty().getHorseHandler().get(0));
        horseRace.setTrack(HorseRaceTrack.TIME_TRACK);
        horseRace.setLaps(1);
        horseRace.setTimeModeEnabled(true);
        horseRace.doEvent(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, caretaker, "Caretaker");
        int time = horseRace.getTimeResultSeconds();
        portraitSay("You finished in " + time + " seconds!");
        if (time < model.getParty().getHorseHandler().getTimedRaceRecord()) {
            model.getParty().getHorseHandler().setTimedRaceRecord(time);
            portraitSay("You broke the record! Here's your money.");
            model.getParty().addToGold(20);
            println("The party receives 20 gold!");
        } else {
            portraitSay("Better luck next time!");
        }
        println("You leave the horse track.");
    }
}
