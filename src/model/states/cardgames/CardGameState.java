package model.states.cardgames;

import model.GameStatistics;
import model.Model;
import model.races.Race;
import model.states.GameState;
import model.states.cardgames.runny.RunnyCardGame;
import model.states.dailyaction.CardGameNode;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import util.MyStrings;
import view.subviews.CardGameSubView;
import view.subviews.CollapsingTransition;

import java.util.ArrayList;
import java.util.List;

public class CardGameState extends GameState {
    private final CardGameSubView subView;
    private CardGame cardGame;
    private int roundsPlayed = 0;

    public CardGameState(Model model) {
        super(model);
        this.subView = new CardGameSubView();
        this.cardGame = new KnockOutCardGame();//new RunnyCardGame();
        subView.setGame(cardGame);
    }

    @Override
    public GameState run(Model model) {
        print("There are " + MyStrings.numberWord(cardGame.getNumberOfNPCs()) + " players are playing a game of " + cardGame.getName() + ". ");
        model.getTutorial().obols(model);
        if (notEnoughObols(model)) {
            println("However, you do not have the minimum amount of obols required to play (" + cardGame.getMaximumBet() + ").");
        } else {
            print("Do you want to join? (Y/N) ");
            if (yesNoInput()) {
                playCardGame(model);
            }
        }
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private boolean notEnoughObols(Model model) {
        return model.getParty().getObols() < cardGame.getMaximumBet();
    }

    private void playCardGame(Model model) {
        cardGame.addPlayer(model.getParty().getLeader(), model.getParty().getObols());
        CollapsingTransition.transition(model, subView);
        print("You sit down to play a game of " + cardGame.getName() + ". Press enter to continue.");
        model.getTutorial().cardGameRunny(model);
        waitForReturn();
        do {
            roundsPlayed++;
            cardGame.replacePlayersLowOnObols(model, this);
            cardGame.addMorePlayers(model, this);
            if (cardGame.getPlayers().size() < 2) {
                println("There's nobody left to play " + cardGame.getName() + " with now.");
                break;
            }
            cardGame.setup(this);
            waitForReturn();
            GameStatistics.incrementCardGamesPlayed();
            cardGame.playRound(model, this);
            synchObols(model, cardGame.getPlayerObols());
            model.getParty().addToObols(cardGame.getPlayerObols());
            if (notEnoughObols(model)) {
                println("You do not have the minimum amount of obols required (" + cardGame.getMaximumBet() + ") to play another round.");
                print("Press enter to continue.");
                waitForReturn();
                break;
            } else {
                print("Do you wish to play another round? (Y/N) ");
            }
        } while (yesNoInput());
        synchObols(model, cardGame.getPlayerObols());
        cardGame.removePlayer();
        println("You leave the card game.");
    }

    private void synchObols(Model model, int playerObols) {
        model.getParty().addToObols(-model.getParty().getObols());
        model.getParty().addToObols(playerObols);
    }

    public void addHandAnimation(CardGamePlayer currentPlayer, boolean cardIn, boolean cardOut, boolean coin) {
        subView.addHandAnimationFor(currentPlayer, cardIn, cardOut, coin);
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!subView.handAnimationHalfWay());
    }

    public void waitForAnimationToFinish() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!subView.handAnimationDone());
    }

    public void addCardDealtAnimation(CardGamePlayer p) {
        subView.addCardDealtAnimation(p);
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!subView.cardDealtAnimationDone());
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public int getNumberOfPlayers() {
        return cardGame.getPlayers().size();
    }
}
