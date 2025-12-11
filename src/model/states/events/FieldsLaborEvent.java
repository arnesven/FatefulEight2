package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.weapons.*;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public abstract class FieldsLaborEvent extends FarmerEvent {
    private final String introText;
    private final String failText;
    private boolean freeRations;

    public FieldsLaborEvent(Model model, String introText, String failText) {
        super(model);
        freeRations = false;
        this.introText = introText;
        this.failText = failText;
    }

    @Override
    public String getDistantDescription() {
        return "a farmer working on his farm";
    }

    protected abstract boolean makeSkillRolls(Model model);

    @Override
    public boolean isFreeRations() {
        return freeRations;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println(introText);
        boolean succ = makeSkillRolls(model);
        if (succ) {
            success(model);
        } else {
            failure(model, failText);
        }
        return false;
    }


    private void failure(Model model, String sitch) {
        println("The party just made a mess of things " + sitch + ". The farmer is appalled by " +
                "the party's inability to do even a simple job and angrily asks you to be " +
                "on your way.");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.snobby, new ArrayList<>(), "I'm not one for manual labor anyway.");
        didSay = didSay || randomSayIfPersonality(PersonalityTrait.rude, new ArrayList<>(), "I'm glad I wasn't born a farmer.");
        if (!didSay) {
            model.getParty().randomPartyMemberSay(model,
                    List.of("Jeez, we were only trying to help...",
                            "But I got my clothes dirty!#",
                            "Come on people, let's get out of here.",
                            "Well, this was a waste of time.",
                            "Wow, he seemed pretty angry."));
        }
    }

    private void success(Model model) {
        println("The farmer thanks the party for the help and offers a small bag of coins (20 gold).");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.generous, new ArrayList<>(),
                "The peasants have a hard life. We should hardly ask them to pay us.");
        if (!didSay) {
            randomSayIfPersonality(PersonalityTrait.greedy, new ArrayList<>(), "A measly few coins for all that work?");
        }
        model.getParty().earnGold(MyRandom.randInt(5, 20));
        new GuestEvent(model, getPortrait()).doEvent(model);
        freeRations = true;
        didSay = randomSayIfPersonality(PersonalityTrait.benevolent, new ArrayList<>(),
                "No shame in rolling up your sleeves every once in a while.");
        if (didSay) {
            model.getParty().randomPartyMemberSay(model, List.of("Sweaty, but at we got paid.",
                    "A good days work."));
        }
    }

}
