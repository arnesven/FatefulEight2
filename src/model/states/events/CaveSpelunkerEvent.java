package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.books.SpelunkersNotesBook;
import model.journal.JournalEntry;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.races.AllRaces;
import model.races.Race;
import model.states.DailyEventState;
import model.tasks.DestinationTask;
import model.tasks.FatueDestinationTask;
import util.MyLists;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class CaveSpelunkerEvent extends DailyEventState {
    private static final String ALREADY_DONE_KEY = "caveSpelunkerEventDone";
    private final UrbanLocation townOrCity;

    public CaveSpelunkerEvent(Model model, UrbanLocation townOrCityClosestToFatue) {
        super(model);
        this.townOrCity = townOrCityClosestToFatue;
    }

    @Override
    public boolean exclusiveToOriginalWorld() {
        return true;
    }

    public static DailyEventState generateEvent(Model model) {
        if (!model.isInOriginalWorld()) {
            return new CaveSpelunkerEvent(model, null);
        }

        if (alreadyDone(model) || calculateAverageLevel(model) < 2.5 ||
                model.getCurrentHex().getLocation() instanceof UrbanLocation) {
            return null;
        }
        Point fatuePos = model.getCaveSystem().getFatuePosition();
        model.getWorld().dijkstrasByLand(fatuePos, true);
        List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle();
        UrbanLocation townOrCityClosestToFatue = (UrbanLocation) model.getWorld().getHex(path.get(path.size()-1)).getLocation();

        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), true);
        path = model.getWorld().shortestPathToNearestTownOrCastle();
        UrbanLocation townOrCityClosestToParty = (UrbanLocation) model.getWorld().getHex(path.get(path.size()-1)).getLocation();
        if (townOrCityClosestToParty != townOrCityClosestToFatue) {
            return null;
        }
        return new CaveSpelunkerEvent(model, townOrCityClosestToFatue);
    }

    private static boolean alreadyDone(Model model) {
        return model.getSettings().getMiscFlags().containsKey(ALREADY_DONE_KEY);
    }

    @Override
    protected void doEvent(Model model) {
        model.getSettings().getMiscFlags().put(ALREADY_DONE_KEY, true);
        Race race = MyRandom.sample(AllRaces.getAllRaces());
        String who = race.getName();
        boolean gender = MyRandom.flipCoin();
        if (race.id() == Race.NORTHERN_HUMAN.id() || race.id() == Race.SOUTHERN_HUMAN.id()) {
            who = manOrWoman(gender);
        }
        println("You're taking a short break at the side of the path when a " +
                "sudden rustling of bushes startles you. Suddenly a " + who + " crawls out of the underbrush.");
        leaderSay("Who... don't sneak up on us like that!");
        println(heOrSheCap(gender) + " looks completely emaciated. " + heOrSheCap(gender) + " tries to stand but is struggling to even get up.");
        CharacterAppearance appearance = PortraitSubView.makeRandomPortrait(Classes.TRAVELLER, race, gender);
        showExplicitPortrait(model, appearance, "Spelunker");
        portraitSay("Water... food... please help me...");
        if (model.getParty().getFood() > 0) {
            print("Do you give the " + who + " some of your rations? (Y/N) ");
            if (yesNoInput()) {
                println("You quickly pull out some bread and water and help the " + who + " sit up.");
                model.getParty().addToFood(-1);
            } else {
                leaderSay("Uhm... sorry, we're all out...");
                randomSayIfPersonality(PersonalityTrait.generous, List.of(model.getParty().getLeader()),
                        "What's wrong with you " + model.getParty().getLeader().getFirstName() + "?");
            }
        } else if (model.getParty().getLeader().hasPersonality(PersonalityTrait.stingy)) {
            leaderSay("I wouldn't give you any even if I had some!");
        } else {
            leaderSay("I'm sorry, we have nothing ourselves.");
        }
        println("After a little while the " + who + " seems to have regained enough strength to talk a little.");
        portraitSay("There were six of us... exploring a cave near " + townOrCity.getPlaceName() + ". I'm the only one " +
                "who made it out alive.");
        leaderSay("Was it goblins, or something more sinister?");
        portraitSay("No, no... we were not exploring some ordinary goblins' nest. We had been looking for it for a long time.");
        leaderSay("What were you looking for?");
        portraitSay("The fortress... the Fortress at the Utmost edge.");
        leaderSay("Perhaps you should rest a little...");
        println("As you attempt to cover the spelunker with a blanket you realize " + hisOrHer(gender) + 
                " breaches are wet, soaked in fact, with blood.");
        leaderSay("You are wounded... we must dress your wounds.");
        portraitSay("I fear it is too late, I'm done for.");
        leaderSay("Nonsense, we've seen worse wounds in our adventures.");
        portraitSay("Listen to me... I'm a dead " + manOrWoman(gender) + " talking. But with my final breath I'll " +
                "warn others not to make the same mistake I did. If you ever find the Fortress at the Utmost edge, " +
                "turn around, run the other way!");
        leaderSay("Calm down... you...");
        println("The life leaves the eyes of the " + who + ".");
        GameCharacter rando = model.getParty().getRandomPartyMember();
        partyMemberSay(rando, heOrSheCap(gender) + " dead...");

        if (!MyLists.any(model.getParty().getDestinationTasks(), (DestinationTask dt) -> dt instanceof FatueDestinationTask)) {
            model.getParty().addDestinationTask(
                    new FatueDestinationTask(model.getWorld().getPositionForLocation((HexLocation) townOrCity), townOrCity));
            JournalEntry.printJournalUpdateMessage(model);
        }

        println("You bury the " + who + " after going through " + hisOrHer(gender) + " things. There's not much there, " +
                "just a few torn clothes, rags really. You do however, find an odd notebook.");
        model.getParty().getInventory().add(new SpelunkersNotesBook());
    }
}
