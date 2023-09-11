package model.states;

import model.Model;
import model.races.Race;
import model.states.cardgames.CardGame;
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
        waitForReturn();
        cardGame.setup();
        waitForReturn();
    }
}
