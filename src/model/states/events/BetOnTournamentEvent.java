package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.map.CastleLocation;
import util.MyPair;
import util.MyRandom;
import view.subviews.TournamentSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetOnTournamentEvent extends TournamentEvent  {

    private List<MyPair<GameCharacter, TournamentOdds>> placedBets = new ArrayList<>();

    public BetOnTournamentEvent(Model model, CastleLocation castle) {
        super(model, castle);
    }

    @Override
    protected void doEvent(Model model) {
        leaderSay("I think we'll just watch this time. Did you say something about placing bets?");
        showOfficial();
        portraitSay("Yes, we accept wagers here. But you will have to wait until all participants have signed up. " +
                "When we have all our fighters we can give you betting odds on each one. We even update our odds during the " +
                "matches.");
        leaderSay("Interesting, we'll come back in a little while then.");
        portraitSay("Please do!");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println("You wander around the tournament area for a while.");
        new FoodStandsEvent(model).doEvent(model);
        setCurrentTerrainSubview(model);
        println("You walk back over to the booth. Now you can see that eight slips of paper have been put up on " +
                "a large board next to the booth.");
        showOfficial();
        portraitSay("This is the match tree. " +
                "It shows who will fight whom and what the outcome has been as the fights conclude, " +
                "Please have a look at it now.");
        waitForReturn();
        List<GameCharacter> fighters = makeFighters(model, 8);
        tournamentViewMenu(model, fighters, new ArrayList<>());

        List<GameCharacter> winners = new ArrayList<>();
        for (int i = 0; i < 7; ++i) {
            GameCharacter fighterB = fighters.remove(fighters.size() - 1);
            GameCharacter fighterA = fighters.remove(fighters.size() - 1);
            GameCharacter winner = performOneFight(model, fighterA, fighterB);
            winners.add(0, winner);
            List<GameCharacter> current = new ArrayList<>(fighters);
            current.addAll(winners);
            if (i == 0) {
                announcerSay("Please get up and stretch your legs as we prepare for the next match. " +
                        "Why not place some bets while you are at it?");
            } else if (i == 3) {
                announcerSay("That concludes the quarter matches. We will now have a longer break before we proceed " +
                        "to the semi-finals.");
            } else if (i == 5) {
                announcerSay("We've had our semi-finals. Now only two fighters remain! We will now take a break before " +
                        "the final fight. How exciting!");
            } else if (i == 6) {
                break;
            } else {
                announcerSay("Please get up and stretch your legs as we prepare for the next match.");
            }
            println("You get up from your seats and walk over to the board next to the booth where you signed up " +
                    "for the tournament. It has already been updated.");
            model.getLog().waitForAnimationToFinish();
            tournamentViewMenu(model, current, winners);
            if (i == 3 || i == 5) {
                for (GameCharacter gc : current) {
                    gc.addToHP(MyRandom.randInt(1, 5));
                }
                fighters = new ArrayList<>(current);
                winners.clear();
            }
        }
        announcerSay("Ladies and gentlemen, we have a winner of the tournament! It's " + winners.get(0).getName() + "!");

    }

    private void tournamentViewMenu(Model model, List<GameCharacter> fighters, List<GameCharacter> knownFighters) {
        Map<GameCharacter, TournamentOdds> odds = calculateOdds(fighters);
        TournamentSubView tournamentSubView = new TournamentSubView(fighters, odds);
        tournamentSubView.setFightersAsKnown(knownFighters);
        model.setSubView(tournamentSubView);
        int timeLeft = 6;
        do {
            waitForReturnSilently();
            if (tournamentSubView.getTopIndex() == 1) {
                timeLeft = 1;
            } else if (tournamentSubView.getTopIndex() == 0) {
                println("There is " + (--timeLeft) * 5 + " minutes until the next fight begins.");
            } else {
                Point pos = tournamentSubView.getCursorPosition();
                GameCharacter fighter = tournamentSubView.getSelectedFighter();
                int sel = multipleOptionArrowMenu(model, pos.x + 2, pos.y + 4, List.of("Bet on", "Find Info", "Back"));
                switch (sel) {
                    case 0:
                        betOnFighter(model, fighter, odds);
                        timeLeft--;
                        break;
                    case 1:
                        List<GameCharacter> notBenched = new ArrayList<>(model.getParty().getPartyMembers());
                        notBenched.removeAll(model.getParty().getBench());
                        if (notBenched.size() == 1) {
                            partyMemberSay(notBenched.get(0), "Somebody's got to stay here...");
                        } else {
                            if (tournamentSubView.isFighterKnown(fighter)) {
                                println("You already know everything about " + fighter.getName() + ".");
                            } else {
                                MyPair<Boolean, GameCharacter> success =
                                        model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.SeekInfo, 6);
                                if (success.first) {
                                    println(success.second.getFirstName() + " finds out the details about " + fighter.getName() + ".");
                                    tournamentSubView.setSelectedFighterKnown();
                                } else {
                                    partyMemberSay(success.second, "Can't find " + himOrHer(fighter.getGender()) + " anywhere...");
                                }
                                model.getParty().benchPartyMembers(List.of(success.second));
                                timeLeft--;
                            }
                        }
                        break;
                    default:
                }
            }
        } while (timeLeft > 1);
        model.getParty().unbenchAll();
        setCurrentTerrainSubview(model);
        println("With only five minutes left to the next fight you hurry " +
                "over to the fighting pit. You take your places and the fight begins.");
    }

    private void betOnFighter(Model model, GameCharacter fighter, Map<GameCharacter, TournamentOdds> odds) {
        println("The odds for " + fighter.getName() + " are currently " + odds.get(fighter).getOddsString() + ".");
        print("How much would you like to bet on " + fighter.getName() + "? ");
        int bet = integerInput();
        if (0 < bet && bet <= model.getParty().getGold()) {
            model.getParty().addToGold(-bet);
            placedBets.add(new MyPair<>(fighter, odds.get(fighter)));
            println("Official: \"I've registered your wager. Here's your ticket.\"");
        } else if (0 < bet) {
            println("You cannot afford that.");
        }
    }

    private int integerInput() {
        while (true) {
            try {
                return Integer.parseInt(lineInput());
            } catch (NumberFormatException nfe) {
                print("Please enter an integer. ");
            }
        }
    }

    private Map<GameCharacter, TournamentOdds> calculateOdds(List<GameCharacter> fighters) {
        Map<GameCharacter, TournamentOdds> result = new HashMap<>();
        int sum = 0;
        for (GameCharacter fighter : fighters) {
            sum += super.calculateFighterStrength(fighter);
        }
        for (GameCharacter fighter : fighters) {
            result.put(fighter, new TournamentOdds(super.calculateFighterStrength(fighter), sum));
        }
        return result;
    }

}
