package model.states.cardgames.runny;

import model.Model;
import model.races.Race;
import model.states.cardgames.CardGameState;
import model.states.cardgames.*;

public class RunnyCharacterPlayer extends RunnyCardGamePlayer {
    public RunnyCharacterPlayer(String name, boolean gender, Race race, int obols) {
        super(name, gender, race, obols, false);
    }

    @Override
    protected void announceTurn(CardGameState state) {
        state.println("It's your turn. ");
    }

    protected boolean callOrFold(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        ButtonCardGameObject call = new CallCardGameObject();
        ButtonCardGameObject button = runnyCardGame.twoButtonOption(state, call, new FoldCardGameObject(), call,
                "The current bet is at " + runnyCardGame.getCurrentBet() + ". Do you wish to call or fold?");
        button.doAction(model, state, runnyCardGame, this);
        return button != call;
    }

    protected void drawFromDeckOrDiscard(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        CardGameObject deckOrDiscard = null;
        runnyCardGame.setCursorEnabled(true);
        runnyCardGame.getMatrix().setSelectedElement(runnyCardGame.getDeck());
        do {
            state.print("Draw a card from the deck or from the discard pile.");
            state.waitForReturn();
            deckOrDiscard = runnyCardGame.getMatrix().getSelectedElement();
        } while (deckOrDiscard != runnyCardGame.getDeck() && deckOrDiscard != runnyCardGame.getDiscard());
        runnyCardGame.setCursorEnabled(false);
        deckOrDiscard.doAction(model, state, runnyCardGame, this);
    }

    protected void discardFromHand(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        CardGameObject cardToDiscard = null;
        runnyCardGame.setCursorEnabled(true);
        runnyCardGame.getMatrix().setSelectedElement(getCard(2));
        do {
            state.print("Select a card from your hand to discard.");
            state.waitForReturn();
            cardToDiscard = runnyCardGame.getMatrix().getSelectedElement();
        } while (!(cardToDiscard instanceof CardGameCard) || !hasCardInHand((CardGameCard) cardToDiscard));
        runnyCardGame.setCursorEnabled(false);
        cardToDiscard.doAction(model, state, runnyCardGame, this);
    }

    protected void raiseOrPass(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        ButtonCardGameObject pass = new PassCardGameObject();
        ButtonCardGameObject selected = runnyCardGame.twoButtonOption(state, new RaiseCardGameObject(),
                pass, pass, "Would you like to raise the bet, or pass?");
        selected.doAction(model, state, runnyCardGame, this);
    }
}
