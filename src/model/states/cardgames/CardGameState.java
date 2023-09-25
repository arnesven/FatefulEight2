package model.states.cardgames;

import model.Model;
import model.races.Race;
import model.states.GameState;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.runny.RunnyCardGame;
import util.MyRandom;
import util.MyStrings;
import view.subviews.CardGameSubView;
import view.subviews.CollapsingTransition;

import java.util.ArrayList;
import java.util.List;

public class CardGameState extends GameState {
    private final CardGameSubView subView;
    private CardGame cardGame;

    public CardGameState(Model model) {
        super(model);
        this.subView = new CardGameSubView();
    }

    @Override
    public GameState run(Model model) {
        this.cardGame = new RunnyCardGame(makeRandomRaces());
        subView.setGame(cardGame);
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

    private List<Race> makeRandomRaces() {
        List<Race> result = new ArrayList<>();
        int count = MyRandom.randInt(2, RunnyCardGame.MAX_NUMBER_OF_PLAYERS-1);
        for (int i = 0; i < count; ++i) {
            result.add(Race.allRaces[MyRandom.randInt(Race.allRaces.length)]);
        }
        return result;
    }

    private void playCardGame(Model model) {
        cardGame.addPlayer(model.getParty().getLeader(), model.getParty().getObols());
        CollapsingTransition.transition(model, subView);
        print("You sit down to play a game of " + cardGame.getName() + ". Press enter to continue.");
        model.getTutorial().cardGameRunny(model);
        waitForReturn();
        do {
            cardGame.replacePlayersLowOnObols(model, this);
            cardGame.setup(this);
            waitForReturn();
            cardGame.playRound(model, this);
            model.getParty().setObols(cardGame.getPlayerObols());
            if (notEnoughObols(model)) {
                println("You do not have the minimum amount of obols required (" + cardGame.getMaximumBet() + ") to play another round.");
                print("Press enter to continue.");
                waitForReturn();
                break;
            } else {
                print("Do you wish to play another round? (Y/N) ");
            }
        } while (yesNoInput());
        println("You leave the card game.");
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
}
