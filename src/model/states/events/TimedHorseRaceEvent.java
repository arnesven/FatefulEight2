package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.races.Race;
import model.states.DailyEventState;
import model.states.dailyaction.BuyHorseState;
import model.states.horserace.HorseRaceTrack;
import util.MyRandom;
import view.subviews.PortraitSubView;

public class TimedHorseRaceEvent extends DailyEventState {
    private boolean borrowHorse = false;

    public TimedHorseRaceEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to horse track",
                "There's a track just outside of town. People race on horses there");
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
            portraitSay("That's okay. You can borrow one of mine!");
            borrowHorse = true;
        }
        print("Do you want to race? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay(imOrWereCap() + " have some other business. No racing for " + meOrUs() + " today.");
            portraitSay("That's too bad, racing's the best there is.");
            println("You leave the horse track.");
            return;
        }
        if (borrowHorse) {
            portraitSay("Alright then! The record time is " + model.getParty().getHorseHandler().getTimedRaceRecord() +
                    " seconds. Can you bet that?");
            leaderSay("Of course I can.");
        } else {
            portraitSay("Alright then! The record time is " + model.getParty().getHorseHandler().getTimedRaceRecord() +
                    " seconds. If you can beat that, I'll give you 20 gold.");
        }

        GameCharacter chosen = model.getParty().getPartyMember(0);
        if (model.getParty().size() > 1) {
            print("Which party member should ride in the race?");
            chosen = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        }
        Horse horseToUse;
        if (borrowHorse) {
            do {
                horseToUse = HorseHandler.generateHorse();
            } while (!horseToUse.canBeRiddenBy(chosen));
        } else {
            horseToUse = model.getParty().getHorseHandler().getSuitableHorseFor(chosen);
        }
        HorseRacingEvent horseRace = new HorseRacingEvent(model, chosen, horseToUse);
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
            if (borrowHorse) {
                leaderSay("I told you I could do it.");
                portraitSay("Wow, that was some good riding. I'm amazed you don't own a horse!");
                leaderSay("Well...");
                portraitSay("I tell you what. My stables are so full at the moment, I have more horses than " +
                        "I know what to do with. Why don't you buy the one you were just on from me. I'll give you a discount!");
                model.getLog().waitForAnimationToFinish();
                BuyHorseState buyHorse = new BuyHorseState(model, "Caretaker",
                        horseToUse, horseToUse.getCost() / 2 - 3);
                buyHorse.run(model);
            } else {
                portraitSay("You broke the record! Here's your money.");
                model.getParty().earnGold(20);
                println("The party receives 20 gold!");
            }
        } else {
            portraitSay("Better luck next time!");
        }
        println("You leave the horse track.");
    }
}
