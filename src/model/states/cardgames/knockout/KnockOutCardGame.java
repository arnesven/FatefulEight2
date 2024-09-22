package model.states.cardgames.knockout;

import model.Model;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.GameState;
import model.states.cardgames.*;
import util.Arithmetics;
import util.MyLists;
import util.MyRandom;
import view.subviews.ArrowMenuSubView;

import java.awt.*;
import java.util.*;
import java.util.List;

public class KnockOutCardGame extends CardGame {

    public static int LOW_STAKES = 0;
    public static int MEDIUM_STAKES = 1;
    public static int HIGH_STAKES = 2;

    private static final int MAX_NUMBER_OF_PLAYERS = 6;
    private final int maximimBet;
    private CardGamePlayer winner;
    private CardGamePlayer startingPlayer;
    private CardGameCard hiddenCard;
    private HashSet<CardGamePlayer> knockedOutPlayers;
    private HashSet<CardGamePlayer> protectedPlayers = new HashSet<>();

    public KnockOutCardGame(int stakes) {
        super(prefixForStake(stakes) + " Knock-Out",
                makePlayers(stakes, CardGame.makeRandomRaces(MAX_NUMBER_OF_PLAYERS-1)),
                new KnockOutCardGameDeck());
        this.maximimBet = stakes * 15 + 10;
    }

    private static String prefixForStake(int stakes) {
        switch (stakes) {
            case 0:
                return "low stakes";
            case 1:
                return "medium stakes";
        }
        return "high stakes";
    }

    private static List<CardGamePlayer> makePlayers(int stakes, List<Race> randomRaces) {
        List<CardGamePlayer> players = new ArrayList<>();
        for (Race r : randomRaces) {
            players.add(makeKnockOutNPC(stakes, r));
        }
        return players;
    }

    private static CardGamePlayer makeKnockOutNPC(int stakes, Race r) {
        boolean gender = MyRandom.flipCoin();
        if (stakes == 1 || (stakes == 2 && MyRandom.flipCoin())) {
            return new SmartKnockOutNPCPlayer(GameState.randomFirstName(gender), gender, r);
        }
        return new KnockOutNPCPlayer(GameState.randomFirstName(gender), gender, r);
    }

    @Override
    public void setup(CardGameState state) {
        winner = null;
        clearCardsInMatrix();
        MyLists.forEach(getPlayers(), (CardGamePlayer p) -> p.getPlayArea().clear());
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
        state.println("One card is set aside - the hidden card.");
        hiddenCard = getDeck().drawCard();
        Point p = getMatrix().getPositionFor(getDiscard());
        getMatrix().remove(getDiscard());
        hiddenCard.flipCard();
        getMatrix().addElement(p.x, p.y, hiddenCard);

        this.startingPlayer = MyRandom.sample(getPlayers());
        knockedOutPlayers = new HashSet<>();
        protectedPlayers = new HashSet<>();

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
            if (p.numberOfCardsInHand() > 0) {
                addToPlayArea(p, p.getCard(0));
                p.clearCards();
            }
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
        List<CardGamePlayer> playersRemaining = getPlayersRemaining();
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
                " a " + cardGameCard.getText() + ".");
        currentPlayer.removeCard(cardGameCard, this);
        state.addHandAnimation(currentPlayer, true, false, false);
        state.waitForAnimationToFinish();
        super.addToPlayArea(currentPlayer, cardGameCard);
        KnockOutCardGamePlayer knockOutPlayer = (KnockOutCardGamePlayer)currentPlayer;
        switch (cardGameCard.getValue()) {
            case 1: // Cudgel
                knockOutPlayer.makeGuess(model, state, this);
                break;
            case 2: // Spyglass
                knockOutPlayer.lookAtPlayersHand(model, state, this);
                break;
            case 3: // Rapier
                knockOutPlayer.compareWithPlayer(model, state, this);
                break;
            case 4: // Shield
                protectedPlayers.add(currentPlayer);
                break;
            case 5: // Crossbow
                knockOutPlayer.forcePlayerToDiscard(model, state, this);
                break;
            case 6: // Magic Mirror
                knockOutPlayer.switchWithOtherPlayer(model, state, this);
                break;
            case 8: // Scepter
                knockOut(state, knockOutPlayer);
                break;
            default:
                break;
        }
    }

    @Override
    public void doOtherCardAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {
        // Unused for Knock-Out
    }

    @Override
    public int getMaximumBet() {
        return maximimBet;
    }

    @Override
    public void replacePlayersLowOnObols(Model model, CardGameState cardGameState) {
        // TODO: Make not abstract
    }

    @Override
    public void addMorePlayers(Model model, CardGameState cardGameState) {

    }

    @Override
    public void triggerTutorial(Model model) {
        model.getTutorial().cardGameKnockOut(model);
    }

    public void removeFromProtected(KnockOutCardGamePlayer knockOutCardGamePlayer) {
        protectedPlayers.remove(knockOutCardGamePlayer);
    }

    public void knockOut(GameState state, CardGamePlayer player) {
        if (player.numberOfCardsInHand() > 0) {
            forceDiscard(player);
        }
        knockedOutPlayers.add(player);
        String comment = MyRandom.sample(List.of("Drat!", "Darn it.", "Shoot.", "Ahh... I'm out.",
                "It's over for me I guess.", "And I was doing so well.", "How annoying!", "Knocked out?"));
        if (player.isNPC()) {
            state.println(player.getName() + " was knocked out!");
            state.printQuote(player.getName(), comment);
        } else {
            state.println("You have been knocked out of the game.");
            state.leaderSay(comment);
        }
    }

    public void forceDiscard(CardGamePlayer player) {
        addToPlayArea(player, player.getCard(0));
        player.removeCard(player.getCard(0), this);
    }

    public List<CardGamePlayer> getPlayersRemaining() {
        return MyLists.filter(getPlayers(),
                (CardGamePlayer player) -> !knockedOutPlayers.contains(player));
    }

    public List<CardGamePlayer> getTargetablePlayers(boolean includingSelf, CardGamePlayer self) {
        List<CardGamePlayer> result =  MyLists.filter(getPlayersRemaining(), (CardGamePlayer player) -> !protectedPlayers.contains(player));
        if (!includingSelf) {
            result.remove(self);
        }
        return result;
    }

    public int selectCardGuess(Model model, GameState state) {
        List<String> options = new ArrayList<>();
        for (int i = 2; i <= 8; ++i) {
            options.add(KnockOutCardGameDeck.nameForValue(i) + " (" + i + ")");
        }
        int[] selectedAction = new int[1];
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 4, 24, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selectedAction[0] = cursorPos;
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturnSilently();
        return selectedAction[0]+2;
    }

    public CardGameCard getHiddenCard() {
        return hiddenCard;
    }
}
