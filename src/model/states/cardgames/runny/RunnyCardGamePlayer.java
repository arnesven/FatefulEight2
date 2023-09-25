package model.states.cardgames.runny;

import model.Model;
import model.races.Race;
import model.states.cardgames.*;
import util.MyPair;

import java.util.ArrayList;
import java.util.List;

public abstract class RunnyCardGamePlayer extends CardGamePlayer {
    public RunnyCardGamePlayer(String firstName, boolean gender, Race race, int obols, boolean isNPC) {
        super(firstName, gender, race, obols, isNPC);
    }

    @Override
    public final void takeTurn(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        announceTurn(state);
        boolean fold = false;
        if (runnyCardGame.getCurrentBet() > getBet()) {
            fold = callOrFold(model, state, runnyCardGame);
        }
        if (!fold) {
            drawFromDeckOrDiscard(model, state, runnyCardGame);
            discardFromHand(model, state, runnyCardGame);
            if (hasWinningHand()) {
                if (isNPC()) {
                    state.print(getName() + " has won the game. ");
                    runnyCardGame.displayNPCHand(this);
                } else {
                    state.println("You have won the game! ");
                }
            } else if (runnyCardGame.getCurrentBet() < runnyCardGame.getMaximumBet()) {
                raiseOrPass(model, state, runnyCardGame);
            }
        }
    }

    protected abstract void announceTurn(CardGameState state);
    protected abstract boolean callOrFold(Model model, CardGameState state, RunnyCardGame runnyCardGame);
    protected abstract void drawFromDeckOrDiscard(Model model, CardGameState state, RunnyCardGame runnyCardGame);
    protected abstract void discardFromHand(Model model, CardGameState state, RunnyCardGame runnyCardGame);
    protected abstract void raiseOrPass(Model model, CardGameState state, RunnyCardGame runnyCardGame);

    private interface CardCheck {
        boolean inSet(CardGameCard current, CardGameCard previous);
    }

    private List<CardGameCard> findSets(List<CardGameCard> cards, CardCheck check, int length) {
        List<CardGameCard> lockedCards = new ArrayList<>();
        List<CardGameCard> currentSet = new ArrayList<>();
        if (cards.isEmpty()) {
            return lockedCards;
        }
        CardGameCard previous = cards.get(0);
        currentSet.add(previous);
        for (int i = 1; i < cards.size(); ++i) {
            CardGameCard current = cards.get(i);
            if (!check.inSet(current, previous) || currentSet.size() == length) {
                if (currentSet.size() == length) {
                    lockedCards.addAll(currentSet);
                }
                currentSet.clear();
            }
            currentSet.add(current);
            previous = current;
        }
        if (currentSet.size() >= length) {
            lockedCards.addAll(currentSet);
        }
        return lockedCards;
    }

    public MyPair<List<CardGameCard>, List<CardGameCard>> partitionHand() {
        // TODO: This method still can't find all winning hands unfortunately...
        List<CardGameCard> handCards = new ArrayList<>();
        for (int i = 0; i < numberOfCardsInHand(); ++i) {
            handCards.add(getCard(i));
        }
        List<CardGameCard> runs = findSets(handCards,
                (CardGameCard current, CardGameCard previous) ->
                        current.getValue() == previous.getValue() + 1 &&
                        current.getSuit() == previous.getSuit(), 3);
        handCards.removeAll(runs);

        List<CardGameCard> sets = findSets(handCards,
                (CardGameCard current, CardGameCard previous) ->
                        current.getValue() == previous.getValue(), 3);
        handCards.removeAll(sets);

        List<CardGameCard> runPairs = findSets(handCards,
                (CardGameCard current, CardGameCard previous) ->
                        current.getValue() == previous.getValue() + 1 &&
                                current.getSuit() == previous.getSuit(), 2);
        handCards.removeAll(runPairs);
        List<CardGameCard> pairs = new ArrayList<>(runPairs);

        List<CardGameCard> setPairs = findSets(handCards,
                (CardGameCard current, CardGameCard previous) ->
                        current.getValue() == previous.getValue(), 2);
        handCards.removeAll(setPairs);
        pairs.addAll(setPairs);

        System.out.println(" ... Partitioning: ");
        System.out.println(" .... Singles:");
        for (CardGameCard card : handCards) {
            System.out.println("      " + card.getText());
        }
        System.out.println(" .... Pairs:");
        for (CardGameCard card : pairs) {
            System.out.println("      " + card.getText());
        }

        return new MyPair<>(handCards, pairs);
    }

    protected List<CardGameCard> getUnlockedSingles() {
        return partitionHand().first;
    }

    protected List<CardGameCard> getUnlockedPairs() {
        return partitionHand().second;
    }

    protected List<CardGameCard> getUnlockedCards() {
        MyPair<List<CardGameCard>, List<CardGameCard>> partitioning = partitionHand();
        List<CardGameCard> unlocked = new ArrayList<>(partitioning.first);
        unlocked.addAll(partitioning.second);
        return unlocked;
    }

    public boolean hasWinningHand() {
        return getUnlockedCards().isEmpty();
    }

}
