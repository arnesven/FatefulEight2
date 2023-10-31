package model.states.cardgames.runny;

import model.Model;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.cardgames.*;
import model.states.GameState;
import util.Arithmetics;
import util.MyRandom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RunnyCardGame extends CardGame {

    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int MAXIMUM_BET = 30;
    private CardGamePlayer startingPlayer;
    private CardGamePlayer winner = null;
    private Set<CardGamePlayer> foldedPlayers;
    private int currentRound = 0;

    public RunnyCardGame(List<Race> npcRaces) {
        super("Runny", makePlayers(npcRaces), new CardGameDeck());
    }

    private static List<CardGamePlayer> makePlayers(List<Race> npcRaces) {
        List<CardGamePlayer> players = new ArrayList<>();
        for (Race r : npcRaces) {
            players.add(makeRunnyNPC(r));
        }
        return players;
    }

    private static CardGamePlayer makeRunnyNPC(Race r) {
        boolean gender = MyRandom.flipCoin();
        return new RunnyNPCPlayer(GameState.randomFirstName(gender), gender, r, MyRandom.randInt(MAXIMUM_BET, MAXIMUM_BET+20));
    }

    @Override
    public void setup(CardGameState state) {
        winner = null;
        currentRound = 0;
        clearCardsInMatrix(state);
        setDeck(new CardGameDeck());
        setDiscard(new CardPile());
        resetCurrentBet();
        state.println("The deck is shuffled and 6 cards are dealt to each player. ");
        super.dealCardsToPlayers(state,6);
        getDiscard().add(getDeck().remove(0));
        this.startingPlayer = MyRandom.sample(getPlayers());
        foldedPlayers = new HashSet<>();
        state.println("Each player antes 1 obol.");
        for (CardGamePlayer p : getPlayers()) {
            state.addHandAnimation(p, false, false, true);
            p.addToBet(1);
        }
        addToCurrentBet(1);
        state.println(startingPlayer.getName() + " will start the game. Press enter to continue.");
    }

    private void clearCardsInMatrix(CardGameState state) {
        for (CardGameObject obj : new ArrayList<>(getMatrix().getElementList())) {
            if (obj instanceof CardGameCard) {
                getMatrix().remove(obj);
            }
        }
    }

    @Override
    public void playRound(Model model, CardGameState state) {
        for (int playerIndex = getPlayers().indexOf(startingPlayer); !checkForWin(model, state);
                playerIndex = Arithmetics.incrementWithWrap(playerIndex, getPlayers().size())) {
            CardGamePlayer currentPlayer = getPlayers().get(playerIndex);
            if (currentPlayer == startingPlayer) {
                currentRound++;
            }
            if (!foldedPlayers.contains(currentPlayer)) {
                currentPlayer.takeTurn(model, state, this);
            }
        }
        if (winner.isNPC()) {
            winner.clearCards();
            state.printQuote(winner.getName(),
                    MyRandom.sample(List.of("I'm good at this",
                    "I knew I had it in me.",
                    "Hah! Losers!",
                    "Sweet obols...",
                    "You just don't stand a chance.",
                    "Looks like those obols are mine.",
                    "Huzzah!", "At last!", "Wohoo, I won!",
                    "Perfection.", "I love this game!",
                    "That was equal parts luck and skill.")));
            state.print(winner.getName() + " wins");
        } else {
            state.print("You win");
        }
        int winPot = makeWinPot();
        state.println(" the pot of " + winPot + " obols.");
        winner.addToObols(winPot);
    }

    @Override
    public void foldPlayer(Model model, CardGameState state, CardGamePlayer player) {
        foldedPlayers.add(player);
        if (!player.isNPC()) {
            removeCardsFromArea(player);
        }
        player.clearCards();
        state.printQuote(player.getName(),
                MyRandom.sample(List.of("Too expensive for me",
                        "I'm out.", "Nah, it's not worth it.",
                        "I fold.", "I give in.", "My cards are rubbish.",
                        "This isn't going well, better quit now.")));
    }

    @Override
    protected CardGamePlayer makeCharacterPlayer(GameCharacter leader, int obols) {
        return new RunnyCharacterPlayer(leader.getFirstName(), leader.getGender(), leader.getRace(), obols);
    }

    private int makeWinPot() {
        int sum = 0;
        for (CardGamePlayer player : getPlayers()) {
            sum += player.getBet();
            player.resetBet();
        }
        return sum;
    }

    private boolean checkForWin(Model model, CardGameState state) {
        for (CardGamePlayer pl : getPlayers()) {
            if (!foldedPlayers.contains(pl) && ((RunnyCardGamePlayer)pl).hasWinningHand()) {
                winner = pl;
                return true;
            }
        }
        if (foldedPlayers.size() == getPlayers().size()-1) {
            List<CardGamePlayer> remaining = new ArrayList<>(getPlayers());
            remaining.removeAll(foldedPlayers);
            winner = remaining.get(0);
            return true;
        }
        return false;
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

    public void displayNPCHand(RunnyCardGamePlayer runnyCardGamePlayer) {
        int col = getMatrix().getColumns() / 2 - 3;
        int row = getMatrix().getRows() / 2 + 1;
        for (int i = 0; i < runnyCardGamePlayer.numberOfCardsInHand(); ++i) {
            CardGameCard card = runnyCardGamePlayer.getCard(i);
            getMatrix().addElement(col + i, row, card);
        }
    }

    @Override
    public int getMaximumBet() {
        return MAXIMUM_BET;
    }

    @Override
    public void replacePlayersLowOnObols(Model model, CardGameState cardGameState) {
        for (int i = 0; i < getPlayers().size(); ++i) {
            CardGamePlayer player = getPlayers().get(i);
            if (player.isNPC()) {
                boolean lowOnObols = player.getObols() < getMaximumBet();
                if (lowOnObols) {
                    cardGameState.println(player.getName() + " is low on obols and leaves the table.");
                }
                boolean gotTired = MyRandom.randInt(40 - cardGameState.getRoundsPlayed()) == 0 &&
                        cardGameState.getRoundsPlayed() > 1;
                if (gotTired) {
                    cardGameState.println(player.getName() + " has had enough and leaves the table.");
                }
                if (lowOnObols || gotTired) {
                    getPlayers().remove(player);
                }
            }
        }
    }

    @Override
    public void addMorePlayers(Model model, CardGameState cardGameState) {
        if (getPlayers().size() < MAX_NUMBER_OF_PLAYERS &&
                MyRandom.randInt(cardGameState.getRoundsPlayed()) == 0 && cardGameState.getRoundsPlayed() > 1) {
            CardGamePlayer replacement;
            do {
                replacement = makeRunnyNPC(Race.randomRace());
            } while (alreadyAtTable(replacement));
            getPlayers().add(0, replacement);
            cardGameState.println(replacement.getName() + " sits down at the table.");
        }
    }

    private boolean alreadyAtTable(CardGamePlayer replacement) {
        for (CardGamePlayer player : getPlayers()) {
            if (player.getName().equals(replacement.getName())) {
                return true;
            }
        }
        return false;
    }

    public int getRound() {
        return currentRound;
    }
}
