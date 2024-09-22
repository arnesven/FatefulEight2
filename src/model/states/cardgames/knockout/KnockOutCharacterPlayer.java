package model.states.cardgames.knockout;

import model.Model;
import model.races.Race;
import model.states.cardgames.*;
import util.MyPair;

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

    @Override
    protected MyPair<CardGamePlayer, Integer> pickPlayerForGuess(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        state.println("Who do you want to target with the Cudgel?");
        CardGamePlayer pickedPlayer = state.selectPlayer(model, knockOutCardGame.getTargetablePlayers(false, this), knockOutCardGame);
        int cardValuePicked = knockOutCardGame.selectCardGuess(model, state);
        return new MyPair<>(pickedPlayer, cardValuePicked);
    }

    @Override
    protected CardGamePlayer pickPlayerForLooking(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        state.println("Who do you want to target with the Spyglass?");
        return state.selectPlayer(model, knockOutCardGame.getTargetablePlayers(false, this), knockOutCardGame);
    }

    @Override
    protected CardGamePlayer pickPlayerForComparing(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        state.println("Who do you want to target with the Rapier?");
        return state.selectPlayer(model, knockOutCardGame.getTargetablePlayers(false, this), knockOutCardGame);
    }

    @Override
    protected CardGamePlayer pickPlayerToForceDiscard(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        state.println("Who do you want to target with the Crossbow?");
        return state.selectPlayer(model, knockOutCardGame.getTargetablePlayers(true, this), knockOutCardGame);
    }

    @Override
    protected CardGamePlayer pickPlayerForSwitch(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        state.println("Who do you want to target with the Magic Mirror?");
        return state.selectPlayer(model, knockOutCardGame.getTargetablePlayers(false, this), knockOutCardGame);
    }

    @Override
    protected void seeCardHook(Model model, CardGameState state, KnockOutCardGame knockOutCardGame,
                               CardGamePlayer player, CardGameCard card) { }

    @Override
    public void runStartOfGameHook(Model model, CardGameState cardGameState, CardGame cardGame) { }
}
