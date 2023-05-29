package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class HighPriestessEvent extends DailyEventState {
    private boolean offended = false;
    private boolean bounce = false;

    public HighPriestessEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.PRI, "High Priestess");
        GameCharacter gc = MyRandom.sample(model.getParty().getPartyMembers());
        println("At first it just seemed like hospitality but now it's clear. " +
                "The High Priestess clearly has the hots for " + gc.getName() + ". She has invited " +
                himOrHer(gc.getGender()) + " to a special tea " +
                "ceremony.");
        model.getParty().partyMemberSay(model, gc, List.of("Uhm, should I play along with this?"));
        leaderSay("This must be handled delicately to not hurt anyone's feelings...");
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, gc, Skill.Entertain, 5, 20, 0);
        if (!result.isSuccessful()) {
            offended = true;
            portraitSay("I am offend! My guards will now have you escorted from my temple. " +
                    "Don't even think about returning here.#");
            model.getParty().banFromTemple(model.getCurrentHex().getLocation().getName());
            new TempleGuardsEvent(model, false).doEvent(model);
        } else {
            portraitSay("I am pleased by your conduct.3");
            println("The High Priestess offers to show you around the temple. What would you like to do?");
            List<DailyEventState> events = List.of(
                    new PriestEvent(model),
                    new WhiteKnightEvent(model),
                    new TranceEvent(model),
                    new CleansingRitual(model),
                    new MeditationEvent(model),
                    new GoldenIdolsEvent(model));
            int res = multipleOptionArrowMenu(model, 30, 20,
                    List.of("Visit Priest", "Visit Paladin", "Chanting Session", "Cleansing Ritual", "Meditation", "Tour of Temple"));
            events.get(res).doTheEvent(model);
        }
    }

    @Override
    public boolean haveFledCombat() {
        return offended;
    }
}
