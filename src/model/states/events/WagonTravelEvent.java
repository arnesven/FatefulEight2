package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import util.MyRandom;

import java.awt.*;
import java.util.List;

public class WagonTravelEvent extends DailyEventState {
    public WagonTravelEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().hasHorses()) {
            new NoEventState(model).doEvent(model);
            return;
        }
        println("A horse and wagon catches up to you on the road. A farmer sits in the saddle.");
        showRandomPortrait(model, Classes.FARMER, "Farmer");
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), false);
        int distance = 0;
        if (MyRandom.rollD10() > 7) {
            System.out.println("Long wagon path");
            distance = 1;
        }
        List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle(distance);
        UrbanLocation townOrCastle = (UrbanLocation) model.getWorld().getHex(path.get(path.size()-1)).getLocation();
        if (model.getParty().size() < 3) {
            portraitSay("Howdy. If you folks are heading to " + townOrCastle.getPlaceName() + ", why don't you hop on?");
            print("Do you accept the ride? (Y/N) ");
            if (yesNoInput()) {
                rideAndThanks(model, path);
            } else {
                leaderSay("Thanks, but our path leads somewhere else.");
                portraitSay("Safe travels friend.");
            }
        } else {
            leaderSay("Where are you heading friend?");
            portraitSay("I'm selling my greens at the market in " + townOrCastle.getPlaceName() + ".");
            print("Do you ask for a ride? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Can you give us a ride?");
                if (model.getParty().size() > 6) {
                    portraitSay("I'm sorry. I don't think my old horse here can pull all that extra weight.");
                    randomSayIfPersonality(PersonalityTrait.unkind, List.of(model.getParty().getLeader()),
                            "No, that creature looks feeble. Are you feeding it enough?");
                    leaderSay("Safe travels friend.");
                } else {
                    portraitSay("Hmm, There's quite a few of you, it will tire my old horse out... " +
                            "But if you could give me some coins I can afford to give him some extra food tonight.");
                    randomSayIfPersonality(PersonalityTrait.stingy, List.of(model.getParty().getLeader()),
                            "What... come on. Just let us ride your cart!");
                    int cost = 5*(distance+1);
                    if (model.getParty().getGold() >= cost) {
                        print("Offer " + cost + " gold? (Y/N) ");
                        if (yesNoInput()) {
                            model.getParty().addToGold(-cost);
                            rideAndThanks(model, path);
                        } else {
                            leaderSay("I think we'll walk then.");
                        }
                    } else {
                        leaderSay("If only we had some coin...");
                        portraitSay("I know what it's like to be without. Okay, hop on anyway I guess.");
                        rideAndThanks(model, path);
                    }
                }
            } else {
                leaderSay("Safe travels friend.");
            }
            println("You part ways with the farmer.");
        }
    }

    private void rideAndThanks(Model model, List<Point> path) {
        forcedMovement(model, path);
        model.getParty().randomPartyMemberSay(model, List.of(
                "Thanks for the ride!", "Good luck at the market.",
                "Why walk when you can ride?", "Bye! Thanks!"));
    }
}
