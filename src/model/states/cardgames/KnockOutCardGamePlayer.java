package model.states.cardgames;

import model.Model;
import model.races.Race;

public abstract class KnockOutCardGamePlayer extends CardGamePlayer {

    public KnockOutCardGamePlayer(String firstName, boolean gender, Race race, int obols, boolean isNPC) {
        super(firstName, gender, race, obols, isNPC);
    }

    @Override
    public void takeTurn(Model model, CardGameState state, CardGame cardGame) {
        KnockOutCardGame knockOutGame = (KnockOutCardGame)cardGame;
        drawFromDeck(model, state, knockOutGame);
        playOneCard(model, state, knockOutGame);
    }

    protected abstract void playOneCard(Model model, CardGameState state, KnockOutCardGame knockOutGame);

    protected void drawFromDeck(Model model, CardGameState state, KnockOutCardGame knockOutGame) {
        knockOutGame.getDeck().doAction(model, state, knockOutGame, this);
    }
}
