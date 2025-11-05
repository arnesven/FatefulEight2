package model.states.events;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.MysteriousMap;
import model.items.potions.IntoxicatingPotion;
import model.items.potions.RumPotion;
import model.states.DailyEventState;
import util.MyLists;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class DrunkenPirateEvent extends DailyEventState {
    public DrunkenPirateEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find drunken captain",
                "There's a pirate captain who's always drunk. Kind of funny but also kind of sad");
    }

    @Override
    protected void doEvent(Model model) {
        println("You pass by an ally, and something stirs in the corner of your eye. " +
                "You flinch, thinking it may be a rat, but realize it's a person lying on the ground.");
        leaderSay("It's a pirate, stinking drunk!");
        model.getLog().waitForAnimationToFinish();
        CharacterAppearance app = PortraitSubView.makeRandomPortrait(Classes.PIRATE_CAPTAIN);
        showExplicitPortrait(model, app, "Pirate Captain");
        println("The pirate is in quite bad shape. " +
                heOrSheCap(app.getGender()) + " gets to " + hisOrHer(app.getGender()) + " feet, but " +
                heOrShe(app.getGender()) + " can barely stand.");
        leaderSay("I almost feel sorry for " + himOrHer(app.getGender()) + ".");
        println("What do you do?");
        List<String> options = new ArrayList<>(List.of("Treat to meal", "Ignore"));
        IntoxicatingPotion booze = (IntoxicatingPotion) MyLists.find(model.getParty().getInventory().getAllItems(),
                it -> it instanceof IntoxicatingPotion);
        if (booze != null) {
            options.add(1, "Give more booze");
        }
        int choice = multipleOptionArrowMenu(model, 24, 24, options);
        if (choice == 0) {
            if (model.getParty().getFood() > 0) {
                model.getParty().addToFood(-1);
            }
            println("You give the pirate some proper food and fresh water to drink. After an hour or two " +
                    heOrShe(app.getGender()) + " is in much better shape.");
            portraitSay("Thank you for helping me. I've had a rough couple of days... or months, come to think of it.");
            println("The Pirate Captain is willing to teach you the ways of buccaneering, ");
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.PIRATE_CAPTAIN);
            change.areYouInterested(model);
        } else if (options.get(choice).contains("booze")) {
            println("You bring out some " + booze.getName() + ", and give it to the pirate.");
            model.getParty().removeFromInventory(booze);
            if (booze instanceof RumPotion) {
                portraitSay("Shhheeyyaaah... Thaasht the stufff.");
                println("The pirate passes out, with a wide smile on " + hisOrHer(app.getGender()) + " face.");
                print("Do you search the unconscious pirate? (Y/N) ");
                if (yesNoInput()) {
                    println("In a pocket on the inside of the pirate's vest, you find an old parchment.");
                    FindTreasureMapEvent.getMysteriousMap(model, this);
                } else {
                    leaderSay("Best just leave " + himOrHer(app.getGender()) + " be.");
                }
            } else {
                portraitSay("Yeesshhhh... Thanks - hic - shalot!");
            }
        } else {
            leaderSay("Best just leave " + himOrHer(app.getGender()) + " be.");
        }
    }
}
