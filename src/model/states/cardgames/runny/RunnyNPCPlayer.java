package model.states.cardgames.runny;

import model.Model;
import model.races.Race;
import model.states.cardgames.*;
import util.MyRandom;

import java.util.Collections;
import java.util.List;

public class RunnyNPCPlayer extends RunnyCardGamePlayer {
    public RunnyNPCPlayer(String name, boolean gender, Race race, int obols) {
        super(name, gender, race, obols, true);
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
        if (MyRandom.randInt(10) == 0) {
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
        if (MyRandom.randInt(3) == 0) {  // TODO: Not if already at max bet
            RaiseCardGameObject raise = new RaiseCardGameObject(); 
            raise.doAction(model, state, runnyCardGame, this);
        }
    }
}
