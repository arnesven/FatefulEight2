package model.states.cardgames;

import javazoom.jl.player.Player;
import model.Model;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.GameState;
import util.Arithmetics;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;

import java.util.*;

public class KnockOutCardGame extends CardGame {

    private static final int MAX_NUMBER_OF_PLAYERS = 6;
    private CardGamePlayer winner;
    private CardGamePlayer startingPlayer;
    private HashSet<CardGamePlayer> knockedOutPlayers;

    public KnockOutCardGame() {
        super("Knock-Out",
                makePlayers(CardGame.makeRandomRaces(MAX_NUMBER_OF_PLAYERS-1)),
                new KnockOutCardGameDeck());
    }

    private static List<CardGamePlayer> makePlayers(List<Race> randomRaces) {
        List<CardGamePlayer> players = new ArrayList<>();
        for (Race r : randomRaces) {
            players.add(makeKnockOutNPC(r));
        }
        return players;
    }

    private static CardGamePlayer makeKnockOutNPC(Race r) {
        boolean gender = MyRandom.flipCoin();
        return new KnockOutNPCPlayer(GameState.randomFirstName(gender), gender, r);
    }

    @Override
    public void setup(CardGameState state) {
        winner = null;
        clearCardsInMatrix();
        setDeck(new KnockOutCardGameDeck());
        setDiscard(new CardPile());
        resetCurrentBet();
        state.println("Each player bets " + getMaximumBet() + " obols.");
        for (CardGamePlayer p : getPlayers()) {
            state.addHandAnimation(p, false, false, true);
            p.addToBet(getMaximumBet());
        }

        state.println("The deck is shuffled and 1 card is dealt to each player. ");
        super.dealCardsToPlayers(state,1);

        this.startingPlayer = MyRandom.sample(getPlayers());
        knockedOutPlayers = new HashSet<>();

        state.println(startingPlayer.getName() + " will start the game. Press enter to continue.");
    }

    @Override
    public void playRound(Model model, CardGameState state) {
        for (int playerIndex = getPlayers().indexOf(startingPlayer); !checkForWin(model, state);
             playerIndex = Arithmetics.incrementWithWrap(playerIndex, getPlayers().size())) {
            CardGamePlayer currentPlayer = getPlayers().get(playerIndex);
            if (!knockedOutPlayers.contains(currentPlayer)) {
                currentPlayer.takeTurn(model, state, this);
            }
        }
        MyLists.forEach(getPlayers(), (CardGamePlayer p) -> {
            addToPlayArea(p, p.getCard(0));
            p.clearCards();
        });
        if (winner.isNPC()) {
            state.printQuote(winner.getName(),
                    MyRandom.sample(List.of("I'm good at this",
                            "I knew I had it in me.",
                            "Hah! Losers!",
                            "Sweet obols...",
                            "Come here little obols.",
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

    private boolean checkForWin(Model model, CardGameState state) {
        List<CardGamePlayer> playersRemaining = MyLists.filter(getPlayers(),
                (CardGamePlayer player) -> !knockedOutPlayers.contains(player));
        if (playersRemaining.size() > 1 && !getDeck().isEmpty()) {
            return false;
        }
        playersRemaining.sort((p1, p2) -> getShowdownScore(p2) - getShowdownScore(p1));
        System.out.println("Scores for remaining players:");
        for (CardGamePlayer p : playersRemaining) {
            System.out.println(p.getName() + " " + getShowdownScore(p));
        }
        winner = playersRemaining.get(0);
        return true;
    }

    private int getShowdownScore(CardGamePlayer p) {
        return p.getCard(0).getValue() * 10000 +
                MyLists.intAccumulate(p.getPlayArea(), CardGameCard::getValue);
    }

    @Override
    public void foldPlayer(Model model, CardGameState state, CardGamePlayer player) {

    }

    @Override
    protected CardGamePlayer makeCharacterPlayer(GameCharacter leader, int obols) {
        return new KnockOutCharacterPlayer(leader.getFirstName(), leader.getGender(), leader.getRace(), obols);
    }

    @Override
    public void doCardInHandAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {
        state.println((currentPlayer.isNPC() ? (GameState.heOrSheCap(currentPlayer.getGender()) + " plays") : "You play") +
                " a " + MyStrings.numberWord(cardGameCard.getValue()) + ".");
        currentPlayer.removeCard(cardGameCard, this);
        state.addHandAnimation(currentPlayer, true, false, false);
        state.waitForAnimationToFinish();
        super.addToPlayArea(currentPlayer, cardGameCard);
    }

    @Override
    public void doOtherCardAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {
        // Unused for Knock-Out
    }

    @Override
    public int getMaximumBet() {
        return 25;
    }

    @Override
    public void replacePlayersLowOnObols(Model model, CardGameState cardGameState) {

    }

    @Override
    public void addMorePlayers(Model model, CardGameState cardGameState) {

    }
}
