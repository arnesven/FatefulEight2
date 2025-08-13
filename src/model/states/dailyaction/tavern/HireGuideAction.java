package model.states.dailyaction.tavern;

import model.Model;
import model.map.UrbanLocation;
import model.states.DailyActionState;
import model.states.EveningState;
import model.states.GameState;
import view.subviews.SubView;
import view.subviews.TavernSubView;

import java.awt.*;
import java.util.List;

public class HireGuideAction extends GameState {
    private static final int COST = 5;
    private static final int DAYS = 7;

    public HireGuideAction(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        guideSay(model, this, "Hey, you! Are you heading into the wild? You'll need somebody who knows the country.");
        model.getTutorial().guides(model);

        if (model.getParty().getGold() >= COST) {
            print("Hire the guide for " + DAYS + " days, cost of " + COST + " gold? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("You've got yourself a deal.");
                guideSay(model, this, "You won't regret this.");
                println("You paid " + COST + " gold to the guide.");
                model.getParty().spendGold(COST);
                model.getParty().setGuide(DAYS);
            } else {
                leaderSay("No thanks.");
                guideSay(model, this, "You'll get lost without me!");
                leaderSay("We'll take our chances.");
            }
        } else {
            leaderSay("I'm sorry, can't afford it.");
            guideSay(model, this, "That's too bad. I know a lot of good things to see. Come back when you got more gold.");
            leaderSay("Maybe we will. See ya.");
        }
        return new DailyActionState(model);
    }

    public static void guideSay(Model model, GameState state, String s) {
        SubView view = model.getSubView();
        if (view instanceof TavernSubView) {
            ((TavernSubView)view).addCalloutAtAgentOrGuide(s.length());
        }
        state.printQuote("Guide", s);
    }


    public static void extendContract(Model model, GameState state) {
        state.println("The guide approaches you.");
        model.getWorld().dijkstrasByLand(model.getParty().getPosition());
        List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle();
        UrbanLocation townOrCityClosest = (UrbanLocation) model.getWorld().getHex(path.get(path.size()-1)).getLocation();
        guideSay(model, state, "My contract is up tomorrow.");
        if (townOrCityClosest == model.getCurrentHex().getLocation()) {
            guideSay(model, state, "I'm staying here, but you can find me at the tavern if you want to hire me again");
            state.leaderSay("Okay bye!");
            return;
        } else {
            guideSay(model, state, "I'm heading back to " + townOrCityClosest.getPlaceName() + " unless you want to extend our deal.");
        }
        state.print("Pay the guide another " + COST + " gold to extend the contract " + DAYS + " days? (Y/N) ");
        if (0 < model.getParty().getGold() && model.getParty().getGold() < COST) {
            state.leaderSay("Unfortunately, this party is a little short on gold. " +
                    "How about " + model.getParty().getGold() + " gold?");
            guideSay(model, state, "Fine... I'll stick around for a few more days. " +
                    "It's not like I really have any other prospects out here.");
            state.println("You paid " + model.getParty().getGold() + " gold to the guide.");
            int days = model.getParty().getGold() + 1;
            model.getParty().spendGold(model.getParty().getGold());
            model.getParty().addToGuide(days);
            state.println("The guide will stay with you for an additional " + days + " days.");
        } else if (model.getParty().getGold() >= COST && state.yesNoInput()) {
            state.println("You paid " + COST + " gold to the guide.");
            model.getParty().spendGold(COST);
            model.getParty().addToGuide(DAYS);
            guideSay(model, state, "Glad to be of service.");
            state.println("The guide will stay with you for an additional " + DAYS + " days.");
        } else {
            state.leaderSay("No thanks.");
            guideSay(model, state, "Then this is where are paths diverge. So long.");
            state.leaderSay("Good bye.");
        }
    }
}
