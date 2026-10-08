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
    private static final int UNLOCKED_PAIR_WEIGHT = 3;
    private static final int MAX_HAND_STRENGTH = UNLOCKED_CARD_WEIGHT * 6;

    /**
     * This parameter determines how strong a player thinks
     * a hand should be to call or raise.
     */
    private final int benchmarkThreshold;

    /**
     * This parameter determines how "nervous" the player is.
     * It affects how strong the player thinks a hand should be
     * given how long the game has been going.
     */
    private final int benchmarkRoundFactor;

    /**
     * This parameter determines how willing the player is to
     * call even if his hand is below the benchmark.
     * A higher value will mean the player can accept to call
     * when his own hand is relatively weak.
     */
    private final int weakHandAcceptance;

    /**
     * This parameter determines how often the player will bluff.
     * 1 => every time, 2 => every other time.
     */
    private final int invertedBluffRatio;

    public RunnyNPCPlayer(String name, boolean gender, Race race, int obols) {
        super(name, gender, race, obols, true);
        benchmarkThreshold = MyRandom.randInt(8, 14);
        benchmarkRoundFactor = MyRandom.randInt(2, 4);
        weakHandAcceptance = MyRandom.randInt(2, RunnyCardGame.MAXIMUM_BET/2);
        invertedBluffRatio = MyRandom.randInt(2, 10);
        log("Benchmark Threshold: " + benchmarkThreshold);
        log("Benchmark Round Factor: " + benchmarkRoundFactor);
        log("Weak Hand Acceptance: " + weakHandAcceptance);
        log("Inverted Bluff Ratio " + invertedBluffRatio);
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
        int benchMark = calcRoundStrengthBenchMark(runnyCardGame);
        log("Benchmark is " + benchMark);
        int handStrength = calcHandStrength();
        log("Hand strength is " + handStrength);
        if (runnyCardGame.getCurrentBet() - benchMark > runnyCardGame.getMaximumBet() / 3) {
            log("suspects a bluff...");
            if (handStrength > benchMark || MyRandom.flipCoin()) {
                log("calls");
                new CallCardGameObject().doAction(model, state, runnyCardGame, this);
                return false;
            }
        }
        int bet = calculateSuitableBet(handStrength, runnyCardGame);
        log("Suitable bet for hand is " + bet);
        log("Current bet is " + runnyCardGame.getCurrentBet());
        int diff = runnyCardGame.getCurrentBet() - bet;
        log("Diff is " + diff);
        log("Acceptance is " + weakHandAcceptance);
        if (diff > weakHandAcceptance) {
            log("folds");
            new FoldCardGameObject().doAction(model, state, runnyCardGame, this);
            return true;
        }
        log("calls");
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
        log("is considering raise or pass");
        log("Hand strength is " + handStrength);
        boolean bluffing = false;
        if (MyRandom.randInt(invertedBluffRatio) == 0) {
            handStrength = MyRandom.randInt(handStrength, MAX_HAND_STRENGTH);
            log(" is bluffing... (on 1 in " + invertedBluffRatio + "), fake hand strength of " + handStrength);
            bluffing = true;
        }
        int benchMark = calcRoundStrengthBenchMark(runnyCardGame);
        log("Benchmark is " + benchMark);
        boolean maxReached = runnyCardGame.getMaximumBet() == runnyCardGame.getCurrentBet();
        if (handStrength > benchMark && !maxReached) {
            if (!bluffing) {
                if ((runnyCardGame.getRound() == 1 && MyRandom.flipCoin())) {
                    log(" is underplaying hand (round 1).");
                    return;
                }
                if (MyRandom.randInt(invertedBluffRatio) == 0) {
                    log(" is underplaying hand (bluff).");
                    return;
                }
            }

            int raise = calculateSuitableBet(handStrength, runnyCardGame) - runnyCardGame.getCurrentBet();
            if (raise > 0) {
                RaiseCardGameObject raiseAction = new RaiseCardGameObject(raise);
                raiseAction.doAction(model, state, runnyCardGame, this);
            }
        }
    }

    private int calculateSuitableBet(int handStrength, RunnyCardGame runnyCardGame) {
        int max = runnyCardGame.getMaximumBet();
        double diff = handStrength - calcRoundStrengthBenchMark(runnyCardGame);
        double strengthModifiedByBenchmark = handStrength + diff / 3.0;
        double bet = (strengthModifiedByBenchmark / (double) MAX_HAND_STRENGTH) * runnyCardGame.getMaximumBet();
        return (int)Math.min(Math.ceil(bet), max);
    }

    /**
     * Calculates the player's current benchmark. I.e. how good
     * the player thinks a hand should be this far into the game.
     * @return the benchmark.
     */
    private int calcRoundStrengthBenchMark(RunnyCardGame runnyCardGame) {
        return runnyCardGame.getRound() * benchmarkRoundFactor + benchmarkThreshold;
    }

    /**
     * Calculates the strength of the hand.
     * A hand with all cards locked is worth 6 x UNLOCKED_CARD_WEIGHT points.
     * Each unlocked card removes points from this total.
     * An unlocked card which is part of a pair removes UNLOCKED_PAIR_WEIGHT points or more.
     * Other unlocked cards remove UNLOCKED_CARD_WEIGHT points.
     * @return the calculated strength
     */
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
        return MAX_HAND_STRENGTH - strength;
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

    @Override
    public void runStartOfGameHook(Model model, CardGameState cardGameState, CardGame cardGame) { }
}
