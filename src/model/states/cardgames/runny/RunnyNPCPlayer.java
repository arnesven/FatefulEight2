package model.states.cardgames.runny;

import model.Model;
import model.races.Race;
import model.states.cardgames.CardGameState;
import model.states.cardgames.CallCardGameObject;
import model.states.cardgames.FoldCardGameObject;
import model.states.cardgames.RaiseCardGameObject;
import util.MyRandom;

public class RunnyNPCPlayer extends RunnyCardGamePlayer {
    public RunnyNPCPlayer(String name, boolean gender, Race race, int obols) {
        super(name, gender, race, obols, true);
    }

    @Override
    public void takeTurn(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        state.print(getName() + "'s turn. ");
        if (getBet() < runnyCardGame.getCurrentBet()) {
            if (MyRandom.randInt(10) == 0) {
                new FoldCardGameObject().doAction(model, state, runnyCardGame, this);
            } else {
                new CallCardGameObject().doAction(model, state, runnyCardGame, this);
            }
        }
        runnyCardGame.getDeck().doAction(model, state, runnyCardGame, this);
        getCard(0).doAction(model, state, runnyCardGame, this);
        if (MyRandom.randInt(3) == 0) {
            RaiseCardGameObject raise = new RaiseCardGameObject();
            raise.doAction(model, state, runnyCardGame, this);
        }
    }
}
