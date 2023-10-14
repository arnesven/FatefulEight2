package model.states.events;

import model.Model;
import model.characters.GameCharacter;
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

public abstract class FieldsLaborEvent extends DarkDeedsEvent {
    private final String introText;
    private final String failText;
    private boolean freeRations;
    private CharacterAppearance portrait;

    public FieldsLaborEvent(Model model, String introText, String failText) {
        super(model, "Talk to", MyRandom.randInt(2, 6));
        freeRations = false;
        this.introText = introText;
        this.failText = failText;
    }

    protected abstract boolean makeSkillRolls(Model model);

    @Override
    public boolean isFreeRations() {
        return freeRations;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("The party encounters a farmer.");
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.FARMER);
        showExplicitPortrait(model, portrait, "Farmer");
        return true;
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


    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Farmer", "", portrait.getRace(), Classes.FARMER,
                portrait, Classes.NO_OTHER_CLASSES, new Equipment(randomWeapon()));
    }

    private Weapon randomWeapon() {
        return MyRandom.sample(List.of(new WoodenSpear(), new Trident(), new Glaive(), new Warhammer(), new Broadsword()));
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> list = new ArrayList<>();
        for (int i = MyRandom.randInt(6); i > 0; --i) {
            list.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.FARMER), Classes.FARMER, randomWeapon()));
        }
        return list;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }

    private void failure(Model model, String sitch) {
        println("The party just made a mess of things " + sitch + ". The farmer is appalled by " +
                "the party's inability to do even a simple job and angrily asks you to be " +
                "on your way.");
        model.getParty().randomPartyMemberSay(model,
                List.of("I'm not one for manual labor anyway.",
                        "Jeez, we were only trying to help...",
                        "But I got my clothes dirty!#",
                        "Come on people, let's get out of here.",
                        "Well, this was a waste of time.",
                        "Wow, he seemed pretty angry.",
                        "I'm glad I wasn't born a farmer."));
    }

    private void success(Model model) {
        println("The farmer thanks the party for the help and offers a small bag of coins (20 gold).");
        model.getParty().addToGold(20);
        new GuestEvent(model, portrait).doEvent(model);
        freeRations = true;
        model.getParty().randomPartyMemberSay(model, List.of("Sweaty, but at we got paid.",
                "No shame in rolling up your sleeves every once in a while.",
                "A good days work."));
    }

}
