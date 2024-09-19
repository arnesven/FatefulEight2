package model.states.cardgames;

import model.Model;
import model.races.Race;
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
        do {
            cardToPlay = getCard(MyRandom.randInt(0, 1));
        } while (cardToPlay.getValue() == 8);
        cardToPlay.doAction(model, state, knockOutGame, this);
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
