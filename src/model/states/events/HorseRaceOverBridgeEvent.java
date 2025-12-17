package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.horses.Pony;
import model.horses.Sphinx;
import model.states.GameState;
import model.states.horserace.HorseRaceTrack;
import util.MyLists;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;

public class HorseRaceOverBridgeEvent extends RiverEvent {
    public HorseRaceOverBridgeEvent(Model model) {
        super(model, true);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return false;
    }

    @Override
    protected void doRiverEvent(Model model) {
        showEventCard("Bridge Race", "You come to a small wooden bridge. You're about to cross when the thundering of hooves stop you in your tracks. " +
                "You are almost run over by several riders.");
        leaderSay("You maniacs!");
        println("One of the riders turns around and rides back toward you.");
        showRandomPortrait(model, Classes.FOR, "Rider");
        portraitSay("Good afternoon!");
        leaderSay("You almost killed me!");
        portraitSay("I'm sorry. This road is being used for horse racing today.");
        leaderSay(iOrWeCap() + " just want to get across the river.");
        portraitSay("Ah, I see. Well I won't stop you. However, we would love it if you joined in the race.");
        leaderSay("But I...");
        portraitSay("Don't worry, we have plenty of horses. It's a fine day for racing! What do you say?");
        print("Join in the horse race? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay(iOrWeCap() + " have more pressing matters to see to. Good bye.");
            portraitSay("Farewell then.");
        }
        portraitSay("There's another bridge further down the road, take it to get back to this side. We'll do two laps!");
        println("Who should participate in the horse race?");
        GameCharacter gc = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        HorseRacingEvent horseRace = new HorseRacingEvent(model, gc, new Sphinx());
        horseRace.setTrack(HorseRaceTrack.BRIDGE_TRACK);
        horseRace.setLaps(2);
        for (int i = 0; i < 12; ++i) {
            horseRace.addNPC(GameState.makeRandomCharacter(1));
        }
        SubView oldSubView = model.getSubView();
        horseRace.run(model);
        CollapsingTransition.transition(model, oldSubView);
        if (MyLists.any(MyLists.take(horseRace.getPlacements(), 3), hr -> hr.getCharacter() == gc)) {
            portraitSay("That was some great riding!");
            if (gc.hasPersonality(PersonalityTrait.narcissistic) || gc.hasPersonality(PersonalityTrait.rude)) {
                partyMemberSay(gc, "Bah, it was nothing.");
            } else {
                partyMemberSay(gc, "Yes, I'm quite pleased myself.");
            }
            if (model.getParty().getHorseHandler().isEmpty()) {
                portraitSay("I can't believe a rider such as yourself doesn't have a horse.");
                partyMemberSay(gc, "Yes, that's true.");
                portraitSay("Actually. This steed here needs a new master. I was going to sell him, but I think he's a fitting prize for you today.");
                partyMemberSay(gc, "Are you really giving me a horse?");
                portraitSay("Yes. I think I am.");
                leaderSay("Thank you. We'll take good care of him.");
                Horse h;
                do {
                    h = HorseHandler.generateHorse();
                } while (h instanceof Pony);
                model.getParty().getHorseHandler().addHorse(h);
                println("The party received a " + h.getName() + ".");
            } else {
                portraitSay("Well, you're free to cross the bridge now.");
                leaderSay("Thank you. We'll be on our way.");
            }
        } else {
            portraitSay("That was fun. Sorry the race didn't go better for you.");
            partyMemberSay(gc, "No matter. I wasn't seriously trying to win.");
            portraitSay("Well, you're free to cross the bridge now.");
            leaderSay("Thank you. We'll be on our way.");
        }
    }
}
