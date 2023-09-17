package model.states.cardgames;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.states.CardGameState;
import util.Arithmetics;
import util.MyRandom;
import view.MyColors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RunnyCardGame extends CardGame {

    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    private CardGamePlayer startingPlayer;
    private CardGamePlayer winner = null;
    private Set<CardGamePlayer> foldedPlayers;

    public RunnyCardGame(List<Race> npcRaces) {
        super("Runny", npcRaces, new CardGameDeck());
    }

    @Override
    public void setup(CardGameState state) {
        SteppingMatrix<CardGameObject> matrix = getMatrix();
        super.dealCardsToPlayers(state,6);
        state.println("The deck is shuffled and 6 cards are dealt to each player. ");
        getDiscard().add(getDeck().remove(0));
        this.startingPlayer = MyRandom.sample(getPlayers());
        foldedPlayers = new HashSet<>();
        state.println("Each player antes 1 obol.");
        for (CardGamePlayer p : getPlayers()) {
            state.addHandAnimation(p, false, false, true);
            p.addToBet(1);
        }
        state.println(startingPlayer.getName() + " will start the game. Press enter to continue.");
    }

    @Override
    public void playRound(Model model, CardGameState state) {
        int playerIndex = getPlayers().indexOf(startingPlayer);
        do {
            CardGamePlayer currentPlayer = getPlayers().get(playerIndex);
            if (currentPlayer.isNPC()) {
                takeNPCTurn(model, state, currentPlayer);
            } else {
                takePlayerTurn(model, state, currentPlayer);
            }
            playerIndex = Arithmetics.incrementWithWrap(playerIndex, getPlayers().size());
        } while (!checkForWin(model, state));
        if (winner.isNPC()) {
            state.print(winner.getName());
        } else {
            state.print("You");
        }
        int winPot = makeWinPot();
        state.println(" win the pot of " + winPot + " obols.");
        winner.addToObols(winPot);
    }

    private int makeWinPot() {
        int sum = 0;
        for (CardGamePlayer player : getPlayers()) {
            sum += player.getBet();
            player.resetBet();
        }
        return sum;
    }

    private void takeNPCTurn(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        state.print(currentPlayer.getName() + "'s turn. ");
        if (MyRandom.randInt(3) == 0) {
            RaiseCardGameObject raise = new RaiseCardGameObject();
            raise.doAction(model, state, this, currentPlayer);
        }
        getDeck().doAction(model, state, this, currentPlayer);
        currentPlayer.getCard(0).doAction(model, state, this, currentPlayer);
    }

    private void takePlayerTurn(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        state.println("It's your turn. ");
        drawFromDeckOrDiscard(model, state, currentPlayer);
        discardFromHand(model, state, currentPlayer);
        if (hasWinningHand(currentPlayer)) {
            state.println("You have won the game!");
            winner = currentPlayer;
        } else {
            raiseOrPass(model, state, currentPlayer);
        }
    }

    private boolean hasWinningHand(CardGamePlayer player) {
        int lastValue = -1;
        MyColors lastSuit = null;
        int setCounter = 0;
        int runCounter = 0;
        for (int i = 0; i < player.numberOfCardsInHand(); ++i) {
            CardGameCard card = player.getCard(i);
            if (lastValue == card.getValue()) {
                setCounter++;
            } else if (lastValue == card.getValue()-1 && lastSuit == card.getSuit()) {
                runCounter++;
            } else {
                if (lastValue != -1 && setCounter < 3 && runCounter < 3) {
                    return false;
                } else {
                    setCounter = 0;
                    runCounter = 0;
                }
            }
            lastValue = card.getValue();
            lastSuit = card.getSuit();
        }
        return setCounter >= 3 || runCounter >= 3;
    }

    private void drawFromDeckOrDiscard(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        CardGameObject deckOrDiscard = null;
        setCursorEnabled(true);
        do {
            state.print("Draw a card from the deck or from the discard pile.");
            state.waitForReturn();
            deckOrDiscard = getMatrix().getSelectedElement();
        } while (deckOrDiscard != getDeck() && deckOrDiscard != getDiscard());
        setCursorEnabled(false);
        deckOrDiscard.doAction(model, state, this, currentPlayer);

    }

    private void discardFromHand(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        CardGameObject cardToDiscard = null;
        setCursorEnabled(true);
        do {
            state.print("Select a card from your hand to discard.");
            state.waitForReturn();
            cardToDiscard = getMatrix().getSelectedElement();
        } while (!(cardToDiscard instanceof CardGameCard) || !currentPlayer.hasCardInHand((CardGameCard) cardToDiscard));
        setCursorEnabled(false);
        cardToDiscard.doAction(model, state, this, currentPlayer);
    }

    private void raiseOrPass(Model model, CardGameState state, CardGamePlayer currentPlayer) {
        RaiseCardGameObject raise = new RaiseCardGameObject();
        getMatrix().addElement(4, getMatrix().getRows()-2, raise);
        ButtonCardGameObject pass = new PassCardGameObject();
        getMatrix().addElement(6, getMatrix().getRows()-2, pass);
        CardGameObject button = null;
        getMatrix().setSelectedPoint(pass);
        setCursorEnabled(true);
        do {
            state.print("Would you like to raise the bet, or pass?");
            state.waitForReturn();
            button = getMatrix().getSelectedElement();
        } while (button != raise && button != pass);
        setCursorEnabled(false);
        button.doAction(model, state, this, currentPlayer);

        getMatrix().remove(raise);
        getMatrix().remove(pass);
    }

    private boolean checkForWin(Model model, CardGameState state) {
        return foldedPlayers.size() == getPlayers().size()-1 || winner != null;
    }

    @Override
    public void doCardInHandAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {
        state.println((currentPlayer.isNPC() ? currentPlayer.getName() : "You") + " discards " + cardGameCard.getText() + ".");
        currentPlayer.removeCard(cardGameCard, this);
        state.addHandAnimation(currentPlayer, true, false, false);
        getDiscard().add(cardGameCard);
        state.waitForAnimationToFinish();
    }

    @Override
    public void doOtherCardAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {
        // Unused for Runny
    }
}
