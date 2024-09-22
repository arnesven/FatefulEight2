package model.states.cardgames.knockout;

import model.Model;
import model.races.Race;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGameCard;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.CardGameState;
import util.MyPair;
import util.MyRandom;

import java.util.*;

public class SmartKnockOutNPCPlayer extends KnockOutCardGamePlayer {
    private HashMap<CardGamePlayer, Integer> opponentCards;

    public SmartKnockOutNPCPlayer(String name, boolean gender, Race r) {
        super(name, gender, r, MyRandom.randInt(60, 140), true);
    }

    @Override
    public void runStartOfGameHook(Model model, CardGameState cardGameState, CardGame cardGame) {
        opponentCards = new HashMap<>();
    }

    @Override
    protected void playOneCard(Model model, CardGameState state, KnockOutCardGame knockOutGame) {
        updateOpponentCards(knockOutGame);
        List<CardGameCard> cards = new ArrayList<>(List.of(getCard(0), getCard(1)));
        cards.sort(Comparator.comparing(CardGameCard::getValue));
        CardGameCard cardToPlay = getCardToPlay(cards);
        cardToPlay.doAction(model, state, knockOutGame, this);
    }

    private CardGameCard getCardToPlay(List<CardGameCard> cards) {
        if (cards.get(0).getValue() == 1 && !opponentCards.isEmpty()) { // Knock out with Cudgel
            return cards.get(0);
        }
        if (opponentCards.containsValue(8)) {
            if (cards.get(0).getValue() == 5) { // Knock out by forcing to discard Scepter
                return cards.get(0);
            }
            if (cards.get(1).getValue() == 5) { // Knock out by forcing to discard Scepter
                return cards.get(1);
            }
        }
        if (cards.get(0).getValue() == 3) {
            for (CardGamePlayer p : opponentCards.keySet()) {
                if (opponentCards.get(p) < cards.get(1).getValue()) {
                    return cards.get(0);
                }
            }
        }
        if (cards.get(0).getValue() == 1 && cards.get(1).getValue() == 2) { // Cudgel and spyglass
            return cards.get(1);
        }
        if (cards.get(0).getValue() == 3 && cards.get(1).getValue() == 4) { // Rapier and shield
            return cards.get(1);
        }
        return cards.get(0);
    }

    private void updateOpponentCards(KnockOutCardGame knockOutGame) {
        for (CardGamePlayer p : knockOutGame.getPlayers()) {
            if (opponentCards.containsKey(p)) {
                if (!knockOutGame.getPlayersRemaining().contains(p)) {
                    log(p.getName() + " has been knocked out, forgetting card");
                    opponentCards.remove(p);
                } else if (!p.getPlayArea().isEmpty()) {
                    if (p.getPlayArea().get(p.getPlayArea().size()-1).getValue() == opponentCards.get(p)) {
                        log("Opponent " + p.getName() + " no longer has a " + opponentCards.get(p));
                        opponentCards.remove(p);
                    }
                }
            }
        }

    }

    private void log(String s) {
        System.out.println("Knock-Out AI (" + getName() + "): " + s);
    }

    @Override
    protected MyPair<CardGamePlayer, Integer> pickPlayerForGuess(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        log("Resolving cudgel");
        List<CardGamePlayer> targets = knockOutCardGame.getTargetablePlayers(false, this);
        if (!opponentCards.isEmpty()) {
            log("I know what somebody is holding... let's target that person.");
            List<Map.Entry<CardGamePlayer, Integer>> opposCards = new ArrayList<>(opponentCards.entrySet());
            opposCards.sort(Comparator.comparingInt(Map.Entry::getValue));
            CardGamePlayer opponent = opposCards.get(opposCards.size()-1).getKey();
            if (targets.contains(opponent)) {
                return new MyPair<>(opponent,
                                    opposCards.get(opposCards.size() - 1).getValue());
            }
        }
        return new MyPair<>(MyRandom.sample(targets), MyRandom.randInt(2, 8));
    }

    @Override
    protected CardGamePlayer pickPlayerForLooking(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        log("Resolving spyglass");
        List<CardGamePlayer> targets = knockOutCardGame.getTargetablePlayers(false, this);
        return MyRandom.sample(targets);
    }

    @Override
    protected CardGamePlayer pickPlayerForComparing(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        log("Resolving rapier");
        List<CardGamePlayer> targets = knockOutCardGame.getTargetablePlayers(false, this);
        if (!opponentCards.isEmpty()) {
            log("I know somebody's card, is it lower than mine?");
            for (CardGamePlayer p : opponentCards.keySet()) {
                if (opponentCards.get(p) < getCard(0).getValue() && targets.contains(p)) {
                    log("... yes, " + p.getName() + " has a " + opponentCards.get(p));
                    return p;
                }
            }
        }
        return targets.get(0);
    }

    @Override
    protected CardGamePlayer pickPlayerToForceDiscard(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        log("Resolving crossbow");
        List<CardGamePlayer> targets = knockOutCardGame.getTargetablePlayers(true, this);
        if (opponentCards.containsValue(8)) {
            log("Somebody has the scepter... gonna make'em discard.");
            for (CardGamePlayer p : opponentCards.keySet()) {
                if (opponentCards.get(p) == 8 && targets.contains(p)) {
                    return p;
                }
            }
        }
        if (getCard(0).getValue() == 8) { // I have the Scepter, don't want to force myself to discard it if i can help it.
            log("I have scepter... Is there anybody else I can force to discard?");
            List<CardGamePlayer> others = knockOutCardGame.getTargetablePlayers(false, this);
            if (others.size() < targets.size()) {
                log("  ... yes");
                return MyRandom.sample(others);
            } else {
                log("  .... no... dammit.");
            }
        }
        return targets.get(0);
    }

    @Override
    protected CardGamePlayer pickPlayerForSwitch(Model model, CardGameState state, KnockOutCardGame knockOutCardGame) {
        log("Resolving Magic Mirror.");
        List<CardGamePlayer> targets = knockOutCardGame.getTargetablePlayers(false, this);
        if (!opponentCards.isEmpty()) {
            log("I know someone's card. Scepters?");
            for (CardGamePlayer p : opponentCards.keySet()) {
                if (opponentCards.get(p) == 8 && targets.contains(p)) {
                    return p;
                }
            }
            log("I know someone's card. Cudgels?");
            for (CardGamePlayer p : opponentCards.keySet()) {
                if (opponentCards.get(p) == 1 && targets.contains(p)) {
                    return p;
                }
            }
        }
        return MyRandom.sample(targets);
    }

    @Override
    protected void seeCardHook(Model model, CardGameState state, KnockOutCardGame knockOutCardGame,
                               CardGamePlayer player, CardGameCard card) {
        log("Opponent " + player.getName() + " has a " + card.getValue());
        opponentCards.put(player, card.getValue());
    }
}
