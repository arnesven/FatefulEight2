package model.states.cardgames;

import model.Model;
import view.MyColors;
import view.sprites.CardGameButtonSprite;
import view.sprites.Sprite;

public class RaiseCardGameObject extends ButtonCardGameObject {

    private static final Sprite SPRITE = new CardGameButtonSprite(0x13, MyColors.LIGHT_GREEN);
    private int bet;

    public RaiseCardGameObject(int bet) {
        this.bet = bet;
    }

    public RaiseCardGameObject() {
        this(1);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getText() {
        return "Raise the current bet.";
    }

    @Override
    public void doAction(Model model, CardGameState state, CardGame cardGame, CardGamePlayer currentPlayer) {
        if (!currentPlayer.isNPC()) {
            int maxBet = cardGame.getMaximumBet() - cardGame.getCurrentBet();
            do {
                state.print("How much would you like to raise (max " + maxBet + ")? ");
                String line = state.lineInput();
                try {
                    bet = Integer.parseInt(line);
                    if (bet <= maxBet) {
                        break;
                    }
                } catch (NumberFormatException nfe) {
                    state.println("Please enter an integer.");
                }
            } while (true);
        }
        state.println((currentPlayer.isNPC() ? currentPlayer.getName() : "You") + " raise the current bet.");
        state.addHandAnimation(currentPlayer, false, false, true);
        state.waitForAnimationToFinish();
        currentPlayer.addToBet(bet);
        cardGame.addToCurrentBet(bet);
    }

}
