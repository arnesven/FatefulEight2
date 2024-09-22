package model.states.cardgames.knockout;

import model.Model;
import model.races.Race;
import model.states.GameState;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGameCard;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.CardGameState;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;

import java.util.List;

public abstract class KnockOutCardGamePlayer extends CardGamePlayer {

    public KnockOutCardGamePlayer(String firstName, boolean gender, Race race, int obols, boolean isNPC) {
        super(firstName, gender, race, obols, isNPC);
    }

    @Override
    public void takeTurn(Model model, CardGameState state, CardGame cardGame) {
        state.print(getName() + "'s turn. ");
        KnockOutCardGame knockOutGame = (KnockOutCardGame)cardGame;
        knockOutGame.removeFromProtected(this);
        drawFromDeck(model, state, knockOutGame);
        if (!falseScepterPlayed(model, state, cardGame)) {
            playOneCard(model, state, knockOutGame);
        }
    }

    private boolean falseScepterPlayed(Model model, CardGameState state, CardGame cardGame) {
        int falseIndex = -1;
        if (getCard(0).getValue() == 7) {
            falseIndex = 0;
        } else if (getCard(1).getValue() == 7) {
            falseIndex = 1;
        }
        if (falseIndex == -1) {
            return false;
        }
        int otherIndex = 1 - falseIndex;
        CardGameCard otherCard = getCard(otherIndex);
        if (otherCard.getValue() == 5 || otherCard.getValue() == 6) {
            getCard(falseIndex).doAction(model, state, cardGame, this);
            return true;
        }
        return false;
    }

    protected abstract void playOneCard(Model model, CardGameState state, KnockOutCardGame knockOutGame);
    protected abstract MyPair<CardGamePlayer, Integer> pickPlayerForGuess(Model model, CardGameState state, KnockOutCardGame knockOutCardGame);
    protected abstract CardGamePlayer pickPlayerForLooking(Model model, CardGameState state, KnockOutCardGame knockOutCardGame);
    protected abstract CardGamePlayer pickPlayerForComparing(Model model, CardGameState state, KnockOutCardGame knockOutCardGame);
    protected abstract CardGamePlayer pickPlayerToForceDiscard(Model model, CardGameState state, KnockOutCardGame knockOutCardGame);
    protected abstract CardGamePlayer pickPlayerForSwitch(Model model, CardGameState state, KnockOutCardGame knockOutCardGame);
    protected abstract void seeCardHook(Model model, CardGameState state, KnockOutCardGame knockOutCardGame, CardGamePlayer player, CardGameCard card);

    protected void drawFromDeck(Model model, CardGameState state, KnockOutCardGame knockOutGame) {
        knockOutGame.getDeck().doAction(model, state, knockOutGame, this);
    }

    public void makeGuess(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        if (knockOutCardGame.getTargetablePlayers(false, this).isEmpty()) {
            return;
        }
        MyPair<CardGamePlayer, Integer> playerPicked = pickPlayerForGuess(model, state, knockOutCardGame);
        sayLine(state, playerPicked.first.getName() + ", I think you have a " + MyStrings.numberWord(playerPicked.second) + ".");
        model.getLog().waitForAnimationToFinish();
        if (playerPicked.first.getCard(0).getValue() == playerPicked.second) {
            knockOutCardGame.knockOut(state, playerPicked.first);
        } else {
            ((KnockOutCardGamePlayer)playerPicked.first).sayLine(state,
                    MyRandom.sample(List.of("Nope", "You're wrong.", "Sorry.")));
        }
    }

    public void lookAtPlayersHand(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        if (knockOutCardGame.getTargetablePlayers(false, this).isEmpty()) {
            return;
        }
        CardGamePlayer playerPicked = pickPlayerForLooking(model, state, knockOutCardGame);
        sayLine(state,"I want to look at your card " + playerPicked.getName() + ".");
        model.getLog().waitForAnimationToFinish();
        seeCardHook(model, state, knockOutCardGame, playerPicked, playerPicked.getCard(0));
        if (!isNPC()) {
            state.println("You look at " + playerPicked.getName() + "'s card, it's a " +
                    playerPicked.getCard(0).getText() + ".");
        }
    }

    public void compareWithPlayer(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        if (knockOutCardGame.getTargetablePlayers(false, this).isEmpty()) {
            return;
        }
        CardGamePlayer playerPicked = pickPlayerForComparing(model, state, knockOutCardGame);
        sayLine(state, playerPicked.getName() + ", let's compare cards.");
        model.getLog().waitForAnimationToFinish();
        int thisCard = getCard(0).getValue();
        int otherCard = playerPicked.getCard(0).getValue();
        if (thisCard > otherCard) {
            knockOutCardGame.knockOut(state, playerPicked);
        } else if (otherCard > thisCard) {
            knockOutCardGame.knockOut(state, this);
        } else if (!isNPC() || !playerPicked.isNPC()) {
            state.println("Both of you have " + thisCard + "s!");
        } else {
            seeCardHook(model, state, knockOutCardGame, playerPicked, playerPicked.getCard(0));
            ((KnockOutCardGamePlayer)playerPicked).seeCardHook(model, state, knockOutCardGame, this, getCard(0));
            state.println("Neither player was knocked out.");
        }
    }

    private void sayLine(GameState state, String line) {
        if (isNPC()) {
            state.printQuote(getName(), line);
        } else {
            state.leaderSay(line);
        }
    }

    public void forcePlayerToDiscard(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        if (knockOutCardGame.getTargetablePlayers(true, this).isEmpty()) {
            return;
        }
        CardGamePlayer playerPicked = pickPlayerToForceDiscard(model, state, knockOutCardGame);
        if (playerPicked != this) {
            sayLine(state, playerPicked.getName() + ", I'm forcing you to discard.");
        } else {
            sayLine(state, "I'm going to make myself discard.");
        }
        model.getLog().waitForAnimationToFinish();
        state.addHandAnimation(playerPicked, true, false, false);
        state.waitForAnimationToFinish();
        CardGameCard card = playerPicked.getCard(0);
        knockOutCardGame.forceDiscard(playerPicked);
        if (card.getValue() == 8) {
            state.println((playerPicked.isNPC() ? playerPicked.getName() : "You") + " dropped the Scepter!");
            knockOutCardGame.knockOut(state, playerPicked);
            return;
        }
        state.println("A " + card.getText() + " was discarded.");

        if (!knockOutCardGame.getDeck().isEmpty()) {
            state.println((playerPicked.isNPC() ? (playerPicked.getName() + " draws") : "You draw") + " a replacement card.");
            ((KnockOutCardGamePlayer)playerPicked).drawFromDeck(model, state, knockOutCardGame);
        } else {
            state.println((playerPicked.isNPC() ? (playerPicked.getName() + " gets") : "You get") + " the hidden card.");
            state.addHandAnimation(playerPicked, false, true, false);
            state.waitForAnimationToFinish();
            playerPicked.giveCard(knockOutCardGame.getHiddenCard(), knockOutCardGame);
            knockOutCardGame.getMatrix().remove(knockOutCardGame.getHiddenCard());
        }
    }

    public void switchWithOtherPlayer(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        if (knockOutCardGame.getTargetablePlayers(false, this).isEmpty()) {
            return;
        }
        CardGamePlayer playerPicked = pickPlayerForSwitch(model, state, knockOutCardGame);
        sayLine(state, playerPicked.getName() + ", let's switch cards.");
        model.getLog().waitForAnimationToFinish();
        CardGameCard otherCard = playerPicked.getCard(0);
        playerPicked.removeCard(0, knockOutCardGame);
        CardGameCard thisCard = getCard(0);
        this.removeCard(0, knockOutCardGame);
        playerPicked.giveCard(thisCard, knockOutCardGame);
        this.giveCard(otherCard, knockOutCardGame);
        if (!playerPicked.isNPC()) {
            state.println("You got a " + playerPicked.getCard(0).getText() + ".");
        }
        if (!isNPC()) {
            state.println("You got a " + getCard(0).getText() + ".");
        }
    }

}
