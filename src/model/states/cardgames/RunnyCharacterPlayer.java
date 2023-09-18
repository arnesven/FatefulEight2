package model.states.cardgames;

import model.Model;
import model.races.Race;
import model.states.CardGameState;

public class RunnyCharacterPlayer extends RunnyCardGamePlayer {
    public RunnyCharacterPlayer(String name, boolean gender, Race race, int obols) {
        super(name, gender, race, obols, false);
    }

    @Override
    public void takeTurn(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        state.println("It's your turn. ");
        if (runnyCardGame.getCurrentBet() > getBet()) {
            callOrFold(model, state, runnyCardGame);
        }
        drawFromDeckOrDiscard(model, state, runnyCardGame);
        discardFromHand(model, state, runnyCardGame);
        if (hasWinningHand()) {
            state.println("You have won the game!");
        } else {
            raiseOrPass(model, state, runnyCardGame);
        }
    }

    private void callOrFold(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        ButtonCardGameObject call = new CallCardGameObject();
        ButtonCardGameObject button = runnyCardGame.twoButtonOption(state, call, new FoldCardGameObject(), call,
                "The current bet is at " + runnyCardGame.getCurrentBet() + ". Do you wish to call or fold?");
        button.doAction(model, state, runnyCardGame, this);
    }

    private void drawFromDeckOrDiscard(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        CardGameObject deckOrDiscard = null;
        runnyCardGame.setCursorEnabled(true);
        do {
            state.print("Draw a card from the deck or from the discard pile.");
            state.waitForReturn();
            deckOrDiscard = runnyCardGame.getMatrix().getSelectedElement();
        } while (deckOrDiscard != runnyCardGame.getDeck() && deckOrDiscard != runnyCardGame.getDiscard());
        runnyCardGame.setCursorEnabled(false);
        deckOrDiscard.doAction(model, state, runnyCardGame, this);
    }

    private void discardFromHand(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        CardGameObject cardToDiscard = null;
        runnyCardGame.setCursorEnabled(true);
        do {
            state.print("Select a card from your hand to discard.");
            state.waitForReturn();
            cardToDiscard = runnyCardGame.getMatrix().getSelectedElement();
        } while (!(cardToDiscard instanceof CardGameCard) || !hasCardInHand((CardGameCard) cardToDiscard));
        runnyCardGame.setCursorEnabled(false);
        cardToDiscard.doAction(model, state, runnyCardGame, this);
    }

    private void raiseOrPass(Model model, CardGameState state, RunnyCardGame runnyCardGame) {
        ButtonCardGameObject pass = new PassCardGameObject();
        ButtonCardGameObject selected = runnyCardGame.twoButtonOption(state, new RaiseCardGameObject(),
                pass, pass, "Would you like to raise the bet, or pass?");
        selected.doAction(model, state, runnyCardGame, this);
    }
}
