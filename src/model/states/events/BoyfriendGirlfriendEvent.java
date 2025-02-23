package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.tasks.BoyfriendGirlfriendDestinationTask;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoyfriendGirlfriendEvent extends AbstractBoyfriendGirlfriendEvent {

    public BoyfriendGirlfriendEvent(Model model, GameCharacter mainCharacter) {
        super(model, mainCharacter);
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = getMainCharacter();
        benchAllButMain(model);
        println(main.getName() + " is taking a little time for " + himOrHer(main.getGender()) + "self, and decides " +
                "to browse some market stands. At a fresh produce stand a farmer is selling strawberries.");
        partyMemberSay(main, "Good year for strawberries?");
        AdvancedAppearance farmerPortrait = PortraitSubView.makeRandomPortrait(Classes.FARMER);
        showExplicitPortrait(model, farmerPortrait, "Farmer");
        portraitSay("Well, we didn't get so many. But they're delicious, " +
                "and I've managed to sell a lot. I only have one box left.");
        partyMemberSay(main, "I see... How much...");
        AdvancedAppearance friend = PortraitSubView.makeRandomPortrait(Classes.None, main.getRace());
        println("Suddenly a " + friend.getRace().getName().toLowerCase() +
                " rushes up beside " + main.getFirstName() + ".");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, friend, friend.getRace().getName());
        portraitSay("Do you have any strawberries left? I simply must have some!");
        showExplicitPortrait(model, farmerPortrait, "Farmer");
        portraitSay("Yes, one box... but uh... I think this " + (main.getGender() ? "lady":"gentleman") +
                " had expressed an interest in them.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, friend, friend.getRace().getName());
        portraitSay("But I see no money has changed hands yet. I'll pay double!");
        partyMemberSay(main, "Hey now. I was here first! The early bird gets the worm.");
        portraitSay("You can have your worms. I want the strawberries. Here's the money.");
        showExplicitPortrait(model, farmerPortrait, "Farmer");
        portraitSay("I'm not sure that's fair " + (friend.getGender() ? "ma'am" : "sir") + "...");

        UrbanLocation homeTown = main.getHomeTown(model);
        String homeTownName = homeTown.getPlaceName();
        partyMemberSay(main, "Wait a minute. Don't I know you? You're from " + homeTownName + "?");
        showExplicitPortrait(model, friend, friend.getRace().getName());
        portraitSay("Yeah, that's right. I grew up there... ");
        partyMemberSay(main, "...");
        String friendName = friend.getGender() ? "Hala":"Haldir";
        partyMemberSay(main, friendName + "?");
        println("The expression on the " + friend.getRace().getName().toLowerCase() +
                " changes from a scowl to mild surprise.");
        showExplicitPortrait(model, friend, friendName);
        portraitSay(main.getFirstName() + "? Is that really you. I didn't even recognize you!");
        partyMemberSay(main, "No, I supposed I have changed quite a bit. What has it been, twenty years?");
        portraitSay("Yes, something like that. We were at school together, I remember we used to play all the time.");
        partyMemberSay(main, "Yes. Those were some good times in " + homeTownName + ". I tell you what, why don't you " +
                "let me buy these strawberries, and we can share them while we talk about old times?");
        portraitSay("That sounds delightful!");

        println(friendName + " and " + main.getFirstName() + " spend the rest of the day in a park talking about their " +
                "childhood. They reminisce about their memories and mischief they used to get up to.");
        portraitSay("We were a bit naughty back then!");
        partyMemberSay(main, "We were kids. Kids do stuff like that. So what have you been up to all these years?");
        portraitSay("Well, things got a bit hard for me after school. I left " + homeTownName + " looking for work " +
                "but could never find any work lasting more than a couple of months. I guess I just kind of drifted around " +
                "for a while. I didn't mind so much, to be honest, but life wasn't always easy. Then, a few years back I came " +
                "to this town and was offered a job as an assistant shopkeeper. It doesn't pay much, but it's enough for " +
                "me to be able to rent a little cottage in town. Life is simple and quite. What about you? Has your life been " +
                "filled with adventure, like we used to dream about?");
        partyMemberSay(main, "Something like that. I'm actually part of an adventuring party, we're only in town temporarily.");
        portraitSay("Oh, I see.");
        println(friendName + " seems slightly disappointed.");

        GameCharacter friendCharacter = new GameCharacter(friendName, randomLastName(), main.getRace(), Classes.None,
                friend, new CharacterClass[]{Classes.CAP, Classes.ART, Classes.FOR, Classes.MAG});
        int topic = topicChoice(model, main, friendCharacter);
        println("The two keep talking but evening is soon approaching.");
        portraitSay("I'm afraid I need to run along now. It's a been a pleasure seeing you again " + main.getFirstName() +
                ", won't you please visit me the next time your in town?");
        partyMemberSay(main, "Of course " + friendName + ". I'll make sure to visit you soon again.");
        println(main.getFirstName() + " and " + friendName + " part ways. Each looking back several times, " +
                "but missing each others' glances.");
        main.addToAttitude(friendCharacter, 10);
        friendCharacter.addToAttitude(main, 10);
        UrbanLocation thisTown = (UrbanLocation) model.getWorld().getHex(model.getParty().getPosition()).getLocation();
        model.getParty().addDestinationTask(
                new BoyfriendGirlfriendDestinationTask(new Point(model.getParty().getPosition()),
                        thisTown.getPlaceName(), main, friendCharacter, topic, model.getDay()));
        JournalEntry.printJournalUpdateMessage(model);
        println(main.getFirstName() + " returns to the party.");
        model.getLog().waitForReturn();
        model.getParty().unbenchAll();
    }
}
