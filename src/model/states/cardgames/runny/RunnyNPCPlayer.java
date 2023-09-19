package model.states.cardgames.runny;

import model.Model;
import model.races.Race;
import model.states.cardgames.*;
import util.MyPair;
import util.MyRandom;

import java.util.Collections;
import java.util.List;

public class RunnyNPCPlayer extends RunnyCardGamePlayer {
    private static final int UNLOCKED_CARD_WEIGHT = 6;
    private static final int UNLOCKED_PAIR_WEIGHT = 2;
    private final int benchmarkThreshold;
    private final int benchmarkRoundFactor;
    private final int weakHandAcceptance;
    private final int invertedBluffRatio;

    public RunnyNPCPlayer(String name, boolean gender, Race race, int obols) {
        super(name, gender, race, obols, true);
        benchmarkThreshold = MyRandom.randInt(2, 10);
        benchmarkRoundFactor = MyRandom.randInt(2, 5);
        weakHandAcceptance = MyRandom.randInt(2, RunnyCardGame.MAXIMUM_BET/2);
        invertedBluffRatio = MyRandom.randInt(3, 50);
    }

    @Override
    protected void announceTurn(CardGameState state) {
        state.print(getName() + "'s turn. ");
        log("Cards in hand:");
        for (int i = 0; i < numberOfCardsInHand(); ++i) {
            log("  " + getCard(i).getText());
        }
    }

    protected boolean callOrFold(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        log("considering whether to fold or call.");
        int handStrength = calcHandStrength();
        log("Hand strength is " + handStrength);
        int benchMark = calcRoundStrengthBenchMark(runnyCardGame);
        log("Benchmark is " + benchMark);
        int bet = calculateSuitableBet(handStrength, runnyCardGame);
        int diff = runnyCardGame.getCurrentBet() - bet;
        log("Diff is " + diff);
        log("Acceptance is " + weakHandAcceptance);
        if (diff > weakHandAcceptance) {
            new FoldCardGameObject().doAction(model, state, runnyCardGame, this);
            return true;
        }

        new CallCardGameObject().doAction(model, state, runnyCardGame, this);
        return false;
    }

    @Override
    protected void drawFromDeckOrDiscard(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        boolean takeDiscard = false;
        int noOfUnlockedBefore = getUnlockedCards().size();
        CardGameCard topCard = runnyCardGame.getDiscard().topCard();
        giveCard(topCard, runnyCardGame);
        int noOfUnlockedAfter = getUnlockedCards().size();
        int unlockedSinglesAfter = getUnlockedSingles().size();
        log("considering taking discard card: " + topCard.getText());
        log("Unlocked before " + noOfUnlockedBefore + ", unlocked after: " + noOfUnlockedAfter + ", unlocked singles after: " + unlockedSinglesAfter);
        if (noOfUnlockedAfter < noOfUnlockedBefore ||
                (noOfUnlockedAfter == noOfUnlockedBefore && unlockedSinglesAfter > 0)) {
            takeDiscard = true;
        }
        removeCard(topCard, runnyCardGame);
        if (takeDiscard) {
            log("Taking discard card");
            runnyCardGame.getDiscard().doAction(model, state, runnyCardGame, this);
        } else {
            log("Taking deck card");
            runnyCardGame.getDeck().doAction(model, state, runnyCardGame, this);
        }
    }

    private void log(String s) {
        System.out.println("RUNNY AI: " + getName() + " " + s);
    }

    @Override
    protected void discardFromHand(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        List<CardGameCard> unlockedCards = getUnlockedSingles();
        log("considering which card to discard.");
        if (unlockedCards.isEmpty()) {
            unlockedCards = getUnlockedPairs();
            log("has no singles, will discard a card from a pair.");
        }
        Collections.shuffle(unlockedCards);
        log("will discard " + unlockedCards.get(0).getText());
        unlockedCards.get(0).doAction(model, state, runnyCardGame, this);
    }

    @Override
    protected void raiseOrPass(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        int handStrength = calcHandStrength();
        log("Hand strength is " + handStrength);
        if (MyRandom.randInt(invertedBluffRatio) == 0) {
            handStrength += MyRandom.randInt(UNLOCKED_CARD_WEIGHT*6);
            log(" is bluffing... (on in " + invertedBluffRatio + ") fake hand strength of " + handStrength);
        }
        int benchMark = calcRoundStrengthBenchMark(runnyCardGame);
        log("Benchmark is " + benchMark);
        int max = runnyCardGame.getMaximumBet() - runnyCardGame.getCurrentBet();
        if (handStrength > benchMark && max > 0) {
            int bet = calculateSuitableBet(handStrength, runnyCardGame);
            RaiseCardGameObject raise = new RaiseCardGameObject(bet);
            raise.doAction(model, state, runnyCardGame, this);
        }
    }

    private int calculateSuitableBet(int handStrength, RunnyCardGame runnyCardGame) {
        int max = runnyCardGame.getMaximumBet() - runnyCardGame.getCurrentBet();
        int diff = handStrength - calcRoundStrengthBenchMark(runnyCardGame);
        return Math.min(diff, max);
    }

    private int calcRoundStrengthBenchMark(RunnyCardGame runnyCardGame) {
        return runnyCardGame.getRound() * benchmarkRoundFactor + benchmarkThreshold;
    }

    private int calcHandStrength() {
        MyPair<List<CardGameCard>, List<CardGameCard>> partitioning = partitionHand();
        int strength = partitioning.first.size() * UNLOCKED_CARD_WEIGHT;
        for (int i = 0; i < partitioning.second.size()/2; ++i) {
            CardGameCard card1 = partitioning.second.get(2*i);
            CardGameCard card2 = partitioning.second.get(2*i + 1);
            if (card1.getValue() + 1 == card2.getValue()) { // Run of two
                if (card1.getValue() == 0 || card2.getValue() == CardGameDeck.MAX_VALUE) {
                    strength += UNLOCKED_PAIR_WEIGHT + 2;
                } else {
                    strength += UNLOCKED_PAIR_WEIGHT;
                }
            } else { // Set of two
                if (valueExistsInLockedPartOfHand(card1, card2)) {
                    strength += UNLOCKED_PAIR_WEIGHT + 1;
                } else {
                    strength += UNLOCKED_PAIR_WEIGHT;
                }
            }
        }
        return (UNLOCKED_CARD_WEIGHT * 6) - strength;
    }

    private boolean valueExistsInLockedPartOfHand(CardGameCard card1, CardGameCard card2) {
        for (int j = 0; j < numberOfCardsInHand(); ++j) {
            CardGameCard other = getCard(j);
            if (other != card1 && other != card2 && other.getValue() == card1.getValue()) {
                return true;
            }
        }
        return false;
    }
}
