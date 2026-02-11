package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.accessories.Circlet;
import model.items.clothing.MagesRobes;
import model.items.weapons.GrandStaff;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class HighPriestessEvent extends GeneralInteractionEvent {
    private boolean offended = false;
    private boolean bounce = false;
    private AdvancedAppearance priestessApp;

    public HighPriestessEvent(Model model) {
        super(model, "Talk to", 50, true);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        showEventCard("The party meets the High Priestess.");
        this.priestessApp = PortraitSubView.makeRandomPortrait(Classes.PRI, Race.randomRace(), true);
        showExplicitPortrait(model, priestessApp, "High Priestess");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        GameCharacter gc = MyRandom.sample(model.getParty().getPartyMembers());
        println("At first it just seemed like good hospitality but now it's clear. " +
                        "The High Priestess clearly has the hots for " + gc.getName() + ". She has invited " +
                        himOrHer(gc.getGender()) + " to a special tea " +
                        "ceremony.");
        if (gc.hasPersonality(PersonalityTrait.narcissistic)) {
            model.getParty().partyMemberSay(model, gc, List.of("Well, I guess it can't be helped. " +
                    "I am a rather attractive person after all."));
        } else {
            model.getParty().partyMemberSay(model, gc, List.of("Uhm, should I play along with this?"));
        }
        leaderSay("This must be handled delicately to not hurt anyone's feelings...");
        randomSayIfPersonality(PersonalityTrait.romantic, List.of(model.getParty().getLeader()), "I find this rather romantic actually.");
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
            setFledCombat(events.get(res).haveFledCombat());
        }
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I am the High Priestess here. I am the oracle of the temple and the Alfa and the Omega to the monks who reside here.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("High Priestess", "", priestessApp.getRace(), Classes.PRI,
                priestessApp, Classes.NO_OTHER_CLASSES, new Equipment(new GrandStaff(), new MagesRobes(), new Circlet()));
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> entourage = new ArrayList<>(makeBodyGuards(3, 'C'));
        entourage.add(new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.NOB)));
        entourage.add(0, new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.NOB)));
        entourage.add(new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)));
        entourage.add(0, new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)));
        return entourage;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }

    @Override
    public boolean haveFledCombat() {
        return offended;
    }
}
