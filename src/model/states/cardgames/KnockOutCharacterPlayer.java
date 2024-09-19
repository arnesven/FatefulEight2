package model.states.cardgames;

import model.Model;
import model.races.Race;

public class KnockOutCharacterPlayer extends KnockOutCardGamePlayer {
    public KnockOutCharacterPlayer(String firstName, boolean gender, Race race, int obols) {
        super(firstName, gender, race, obols, false);
    }

    @Override
    protected void playOneCard(Model model, CardGameState state, KnockOutCardGame knockOutGame) {
        CardGameObject cardToDiscard = null;
        knockOutGame.setCursorEnabled(true);
        knockOutGame.getMatrix().setSelectedElement(getCard(0));
        do {
            state.print("Select a card from your hand you wish to play.");
            state.waitForReturn();
            cardToDiscard = knockOutGame.getMatrix().getSelectedElement();
        } while (!(cardToDiscard instanceof CardGameCard) || !hasCardInHand((CardGameCard) cardToDiscard));
        knockOutGame.setCursorEnabled(false);
        cardToDiscard.doAction(model, state, knockOutGame, this);
    }
}
