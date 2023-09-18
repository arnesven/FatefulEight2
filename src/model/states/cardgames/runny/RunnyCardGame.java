package model.states.cardgames.runny;

import model.Model;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.cardgames.CardGameState;
import model.states.GameState;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGameCard;
import model.states.cardgames.CardGameDeck;
import model.states.cardgames.CardGamePlayer;
import util.Arithmetics;
import util.MyRandom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RunnyCardGame extends CardGame {

    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    private CardGamePlayer startingPlayer;
    private CardGamePlayer winner = null;
    private Set<CardGamePlayer> foldedPlayers;

    public RunnyCardGame(List<Race> npcRaces) {
        super("Runny", makePlayers(npcRaces), new CardGameDeck());
    }

    private static List<CardGamePlayer> makePlayers(List<Race> npcRaces) {
        List<CardGamePlayer> players = new ArrayList<>();
        for (Race r : npcRaces) {
            boolean gender = MyRandom.flipCoin();
            players.add(new RunnyNPCPlayer(GameState.randomFirstName(gender), gender, r, MyRandom.randInt(20, 40)));
        }
        return players;
    }

    @Override
    public void setup(CardGameState state) {
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
        addToCurrentBet(1);
        state.println(startingPlayer.getName() + " will start the game. Press enter to continue.");
    }

    @Override
    public void playRound(Model model, CardGameState state) {
        for (int playerIndex = getPlayers().indexOf(startingPlayer); !checkForWin(model, state);
                playerIndex = Arithmetics.incrementWithWrap(playerIndex, getPlayers().size())) {
            CardGamePlayer currentPlayer = getPlayers().get(playerIndex);
            if (!foldedPlayers.contains(currentPlayer)) {
                currentPlayer.takeTurn(model, state, this);
            }
        }
        if (winner.isNPC()) {
            state.println(winner.getName() + " wins");
            winner.clearCards();
            state.println(winner.getName() + ": \"" +
                    MyRandom.sample(List.of("I'm good at this",
                    "I knew I had it in me.",
                    "You just don't stand a chance.",
                    "Looks like those obols are mine.",
                    "Huzzah!", "At last!", "Wohoo, I won!",
                    "Perfection.", "I love this game!",
                    "That was equal parts luck and skill.")) +
                    "\"");
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
        player.clearCards();
        state.println(winner.getName() + ": \"" +
                MyRandom.sample(List.of("Too expensive for me",
                        "I'm out.", "Nah, it's not worth it.",
                        "I fold.", "I give in.", "My cards are rubbish.",
                        "This isn't going well, better quit now.")) +
                "\"");
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
        return foldedPlayers.size() == getPlayers().size()-1;
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
}
