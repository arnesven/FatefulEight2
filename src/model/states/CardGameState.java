package model.states;

import model.Model;
import model.races.Race;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.RunnyCardGame;
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
        print("There are " + MyStrings.numberWord(cardGame.getNumberOfNPCs()) + " players are playing a game of " + cardGame.getName() + ". Do you want to join? (Y/N) ");
        if (yesNoInput()) {
            playCardGame(model);
        }
        return model.getCurrentHex().getEveningState(model, false, false);
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
        waitForReturn();
        cardGame.setup(this);
        waitForReturn();
        cardGame.playRound(model, this);
        waitForReturn();
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
