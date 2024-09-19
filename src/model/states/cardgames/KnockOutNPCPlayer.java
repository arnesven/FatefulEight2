package model.states.cardgames;

import model.Model;
import model.races.Race;
import util.MyRandom;

public class KnockOutNPCPlayer extends KnockOutCardGamePlayer {
    public KnockOutNPCPlayer(String name, boolean gender, Race r) {
        super(name, gender, r, 99, true);
    }

    @Override
    protected void playOneCard(Model model, CardGameState state, KnockOutCardGame knockOutGame) {
        CardGameCard cardToPlay = getCard(MyRandom.randInt(0, 1));
        state.println(getName() + "'s turn.");
        cardToPlay.doAction(model, state, knockOutGame, this);
    }

}
