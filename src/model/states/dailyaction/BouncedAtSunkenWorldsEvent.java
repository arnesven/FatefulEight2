package model.states.dailyaction;

import model.Model;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.List;

public class BouncedAtSunkenWorldsEvent extends DailyEventState {
    public BouncedAtSunkenWorldsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.BANDIT, "Bouncer");
        portraitSay("Hey! You're not getting in here. This joint is for real pirates only!");
        if (model.getParty().getLeader().hasPersonality(PersonalityTrait.jovial)) {
            leaderSay("How do you know we ARRRR'nt!? He he he...");
        } else {
            leaderSay("How do you know we aren't pirates?");
        }
        portraitSay("I can tell just from looking at you! Now get lost landlubbers.");
        leaderSay("Rude.");
        model.getLog().waitForAnimationToFinish();
        println("You're start walking away when you hear a weak voice.");
        CharacterAppearance app = PortraitSubView.makeOldPortrait(Classes.HERMIT, Race.randomRace(), MyRandom.flipCoin());
        showExplicitPortrait(model, app, "Beggar");
        String sirOrMam = (model.getParty().getLeader().getGender() ? "ma'am" : "sir");
        portraitSay("Spare a few coins " + sirOrMam + "?");
        int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Give a few obols", "Give 1 gold", "Ignore beggar"));
        if (choice == 0) {
            if (model.getParty().getObols() == 0) {
                println("You realize you do not have any obols, so you simply walk away from the beggar.");
            } else {
                println("You hand the beggar some obols.");
                model.getParty().addToObols(-Math.min(model.getParty().getObols(), 3));
                portraitSay("The seas be kind to you " + sirOrMam + ".");
                println("You walk away from the beggar.");
            }
        } else if (choice == 1) {
            if (model.getParty().getGold() == 0) {
                println("You realize you do not have any gold, so you simply walk away from the beggar.");
            } else {
                println("You hand the beggar a gold piece.");
                model.getParty().loseGold(1);
                portraitSay("Mighty generous of you " + sirOrMam + ". Say, if you really wanna get " +
                        "into the Sunken Worlds, ya can easily hoodwink that bouncer.");
                leaderSay("Go on.");
                portraitSay("I've seen him let just about anybody in, as long as they're wearin' somethin' piraty.");
                leaderSay("Are you serious?");
                portraitSay("Yeeah. This one lady. Not one day in her life at sea she had. " +
                        "She just stuck a cutlass in her belt and he let her in. Her boyfriend just put on a striped " +
                        "shirt and spoke with a pirate's accent. He got in.");
                leaderSay("I can't believe it. Thanks for the tip.");
                portraitSay("Smooth sailing friend.");
            }
        } else {
            println("You walk away from the beggar.");
        }
    }

}
