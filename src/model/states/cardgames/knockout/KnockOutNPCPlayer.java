package model.states.cardgames.knockout;

import model.Model;
import model.races.Race;
import model.states.cardgames.CardGameCard;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.CardGameState;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KnockOutNPCPlayer extends KnockOutCardGamePlayer {
    public KnockOutNPCPlayer(String name, boolean gender, Race r) {
        super(name, gender, r, 99, true);
    }

    @Override
    protected void playOneCard(Model model, CardGameState state, KnockOutCardGame knockOutGame) {
        CardGameCard cardToPlay;
        cardToPlay = getCard(rankOfCard(0) > rankOfCard(1) ? 0 : 1);
        cardToPlay.doAction(model, state, knockOutGame, this);
    }

    private int rankOfCard(int i) {
        int card = getCard(i).getValue();
        if (card == 8) {
            return Integer.MIN_VALUE;
        }

        return (8 - card) * 10 + MyRandom.randInt(-25, 25);
    }

    @Override
    protected MyPair<CardGamePlayer, Integer> pickPlayerForGuess(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        return new MyPair<>(pickRandomOther(knockOutCardGame, false), MyRandom.randInt(2, 8));
    }

    @Override
    protected CardGamePlayer pickPlayerForLooking(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        return pickRandomOther(knockOutCardGame, false);
    }

    private CardGamePlayer pickRandomOther(KnockOutCardGame knockOutCardGame, boolean includingSelf) {
        List<CardGamePlayer> list = new ArrayList<>(knockOutCardGame.getTargetablePlayers(includingSelf, this));
        list.remove(this);
        Collections.shuffle(list);
        return list.get(0);
    }

    @Override
    protected CardGamePlayer pickPlayerForComparing(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        return pickRandomOther(knockOutCardGame, false);
    }

    @Override
    protected CardGamePlayer pickPlayerToForceDiscard(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        return pickRandomOther(knockOutCardGame, true);
    }

    @Override
    protected CardGamePlayer pickPlayerForSwitch(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        return pickRandomOther(knockOutCardGame, false);
    }
}
