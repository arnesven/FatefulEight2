package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import model.races.Race;
import model.states.DailyEventState;
import util.MyPair;
import util.MyRandom;

import java.util.List;

public class BrawlingPitEvent extends DailyEventState {
    public BrawlingPitEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Brawling Pit", "The party walks through a small farmstead. The farmers have put up a wooden fence here, " +
                "and the locals are hooting and cheering.");
        leaderSay("What's going on here?");
        showRandomPortrait(model, Classes.FARMER, "Farmer");
        boolean fighterGender = MyRandom.flipCoin();
        String fighterName = fighterGender
                ? MyRandom.sample(List.of("Bertha", "Olga", "Tabatha"))
                : MyRandom.sample(List.of("Ragnar", "Alfred", "Karl-Axel"));
        portraitSay("We're taking the day off. " + fighterName + " offered to entertain us.");
        leaderSay("Entertainment? All I see is a fence.");
        portraitSay(heOrShe(fighterGender) + "'s down in the pit. Taking on challengers. " +
                heOrSheCap(fighterGender) + "'s a real wrestler!");
        leaderSay("Uh-huh.");
        randomSayIfPersonality(PersonalityTrait.critical, List.of(), "Probably nothing noteworthy. " +
                "These peasants are impressed by anything.");
        Race fighterRace = MyRandom.sample(List.of(Race.NORTHERN_HUMAN, Race.HALF_ORC, Race.DWARF));
        println("You walk over and look down into the brawling pit. You see a large " +
                fighterRace.getName().toLowerCase() + " wrestling a peasant. Both of them are sweaty and dirty. " +
                "The " + fighterRace.getName().toLowerCase() + " is clearly winning.");
        randomSayIfPersonality(PersonalityTrait.prudish, List.of(), "Ugh, looks dirty.");
        randomSayIfPersonality(PersonalityTrait.playful, List.of(), "But also, kind of fun!");
        portraitSay("Young Snorri is down! " + fighterName + " is the winner! That's three in a row. " +
                "Can nobody beat " + himOrHer(fighterGender) + "?");
        portraitSay("How about you outsider?");
        leaderSay("I don't know...");
        portraitSay("I'll make it worth your while! Five obols if you take " + fighterName + " on. " +
                "Fifteen gold if you take " + himOrHer(fighterGender) + " down.");
        print("Do you challenge " + fighterName + "? (Y/N) ");
        if (yesNoInput()) {
            MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.UnarmedCombat, 7);
            if (result.first) {
                println(fighterName + " comes at " + result.second.getFirstName() + " at full force and speed. " +
                        "But " + result.second.getFirstName() + " just uses that force against " + himOrHer(fighterGender) +
                        ". With brilliant technique, the sweaty " + fighterRace.getName().toLowerCase() +
                        " is thrown to the ground, time and again.");
                partyMemberSay(result.second, "Had enough?");
                portraitSay("It looks like our " + fighterName + " has finally met " + hisOrHer(fighterGender) + " match.");
                portraitSay("Well outsider, you gave us a show. Here's your money, you've earned it.");
                model.getParty().earnGold(15);
                partyMemberSay(result.second, "Anybody else want a go?");
                portraitSay("I think we need to get back to work. Those crops aren't gonna harvest themselves.");
                leaderSay("Don't worry, at least you have plenty of chickens on the farm.");
                portraitSay("Ha ha...");
            } else {
                println(result.second.getFirstName() + " tries " + hisOrHer(result.second.getGender()) +
                        " best, but can not match " + fighterName + " for strength and technique. In the end, " +
                        heOrShe(result.second.getGender()) + " must submit.");
                portraitSay("That was a good attempt. But our " + fighterName + " is just the strongest. Here are your obols.");
                model.getParty().addToObols(5);
                partyMemberSay(result.second, "Phew, I need to get into better shape.");
            }
        } else {
            leaderSay("Perhaps another time.");
            portraitSay("Bawk bawk bawk!");
            if (randomSayIfPersonality(PersonalityTrait.irritable, List.of(), "What was that!?")) {
                portraitSay("Nothing! Just the chickens on my farm making some noise.");
            }
        }
        println("You leave the farmstead.");
    }
}
