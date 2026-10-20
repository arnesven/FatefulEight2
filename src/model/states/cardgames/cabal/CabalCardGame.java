package model.states.cardgames.cabal;

import model.Model;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.GameState;
import model.states.cardgames.*;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.subviews.CabalCardGameSubView;
import view.subviews.CardGameSubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CabalCardGame extends CardGame {

    public static final int MAX_BET = 24;
    private final CabalCardGameSubView subView;
    private List<Boolean> bag;
    private CabalCardGamePlayer pickPlayer;
    private int turn = 0;
    private final List<CabalCard> cardsPickedThisTurn = new ArrayList<>();
    private int starsPicked = 0;
    private int daggersPicked = 0;
    private boolean showStones;
    private boolean showBag = true;

    public CabalCardGame(int numberOfPlayers) {
        super("Cabal", makeNPCPlayers(numberOfPlayers-1), new CabalDeck(numberOfPlayers), false);
        this.subView = new CabalCardGameSubView(this);
    }

    private static List<CardGamePlayer> makeNPCPlayers(int npcs) {
        return fixDuplicateNames(MyLists.transform(makeRandomRaces(npcs, npcs), CabalCardGame::makeCabalNPC));
    }

    @Override
    public CardGameSubView makeSubView() {
        return subView;
    }

    @Override
    public void setup(CardGameState state) {
        payAnte(state);
        setUpBag(state);
        this.turn = 1;
        drawFromBag(state);
        pickPlayer = MyRandom.sample(getCabalPlayers());
        state.println("Press enter to start the game.");
    }

    private void payAnte(CardGameState state) {
        state.println("Each player bets " + getMaximumBet() + " obols.");
        for (CardGamePlayer p : getPlayers()) {
            state.addHandAnimation(p, false, false, true);
            p.addToBet(getMaximumBet());
        }
    }

    private void setUpBag(GameState state) {
        int numberOfBlack = getPlayers().size() == 7 ? 3 : 2;
        this.bag = MyLists.generate(getPlayers().size() - numberOfBlack, () -> false);
        state.println(MyStrings.capitalize(MyStrings.numberWord(bag.size())) +
                " white stones and " + MyStrings.numberWord(numberOfBlack) +
                " black stones are added to the bag.");
        for (int i = 0; i < numberOfBlack; i++) {
            this.bag.add(true);
        }

        for (boolean b : bag) {
            subView.addFallingStoneAnimation(b);
            state.waitUntil(subView, CabalCardGameSubView::fallingStoneAnimationIsDone);
        }

        Collections.shuffle(bag);
        state.println("The bag is shuffled.");
        System.out.println("Bag: " + MyLists.commaAndJoin(bag, b -> b + ""));
    }

    private void drawFromBag(CardGameState state) {
        showBag = true;
        state.println("Each player draws a stone from the bag.");
        for (CardGamePlayer p : getPlayers()) {
            state.addHandAnimation(p, false, false, false);
            state.waitForAnimationToFinish();
            boolean isTraitor = bag.remove(MyRandom.randInt(bag.size()));
            ((CabalCardGamePlayer)p).setTraitor(isTraitor);
            if (p == getCharacterPlayer()) {
                state.println("Your stone is " + (isTraitor ? "black" : "white") + ", you are a " +
                        (isTraitor ? "Traitor" : "Friend") + ".");
            }
            System.out.println(p.getName() + " is a " +  (isTraitor ? "Traitor" : "Friend"));
        }
        showBag = false;
    }

    private void makeStartOfTurnClaim(CardGameState state, int turn) {
        int pickIndex = getCabalPlayers().indexOf(pickPlayer);
        int numPlayers = getCabalPlayers().size();
        List<CabalCardGamePlayer> list = new ArrayList<>(getCabalPlayers().subList(pickIndex, numPlayers));
        list.addAll(getCabalPlayers().subList(0, pickIndex));
        for (CabalCardGamePlayer cp : list) {
            cp.makeStartOfTurnClaim(state, this, turn);
        }
        MyLists.forEach(list, ccp -> ccp.updateSuspicionsForStartOfTurnClaim(state, this));
    }

    private void makeEndOfTurnClaim(CardGameState state, int turn) {
        int pickIndex = getCabalPlayers().indexOf(pickPlayer);
        int numPlayers = getCabalPlayers().size();
        List<CabalCardGamePlayer> list = new ArrayList<>(getCabalPlayers().subList(pickIndex, numPlayers));
        list.addAll(getCabalPlayers().subList(0, pickIndex));
        for (CabalCardGamePlayer cp : list) {
            cp.makeEndOfTurnClaim(state, this, turn);
        }
        MyLists.forEach(list, ccp -> ccp.updateSuspicionsForEndOfTurnClaim(state, this));
    }

    public List<CabalCardGamePlayer> getCabalPlayers() {
        return MyLists.transform(getPlayers(), p -> (CabalCardGamePlayer)p );
    }

    @Override
    public void playRound(Model model, CardGameState state) {
        dealCardsToPlayers(state, 5);
        state.println(pickPlayer.getName() + " will start the claiming.");
        makeStartOfTurnClaim(state, 1);
        state.println(pickPlayer.getName() + " has the first pick.");
        do {
            for (int pick = 0; pick < getCabalPlayers().size(); ++pick) {
                pickPlayer.takeTurn(model, state, this);
                model.getLog().waitForAnimationToFinish();
                if (gameIsOver()) {
                    break;
                }
            }
            if (gameIsOver()) {
                break;
            }
            state.println("End of turn " + turn + ". ");
            if (turn < 4) {
                makeEndOfTurnClaim(state, turn);
            }
            turn++;
            if (turn == 5) {
                state.println("The game is over.");
                break;
            }
            state.println("Reclaiming all unplayed cards. Press enter to continue");
            state.waitForReturn();
            recallAllCards();
            countPickedCards();
            MyLists.forEach(getCabalPlayers(), CabalCardGamePlayer::clearClaim);
            if (turn == 4) {
                MyLists.forEach(getCabalPlayers(), ccp -> ccp.drawTeamConclusions(this));
            }
            dealCardsToPlayers(state, 6 - turn);
            makeStartOfTurnClaim(state, turn);
        } while (true);

        List<CabalCardGamePlayer> winners;
        if (allStarsHaveBeenDrawn()) {
            state.println("The Friends have drawn out all the stars!");
            winners = MyLists.filter(getCabalPlayers(),
                    cc -> !cc.isTraitor());
        } else {
            state.println("The Traitors are the winners!");
            winners = MyLists.filter(getCabalPlayers(),
                    CabalCardGamePlayer::isTraitor);
        }
        MyLists.forEach(getCabalPlayers(), CabalCardGamePlayer::clearClaim);
        state.println("The stones and remaining cards are revealed.");
        this.setShowStonesAndCards(true);
        state.println(MyLists.commaAndJoin(winners, CardGamePlayer::getName) +
                " share the pot of " + getPotSize() + " obols. Press enter to continue.");
        for (CardGamePlayer cgp : winners) {
            cgp.addToObols(getPotSize() / winners.size());
        }
        state.waitForReturn();
    }

    private void countPickedCards() {
        for (CabalCard cc : cardsPickedThisTurn) {
            if (cc instanceof DaggerCabalCard) {
                daggersPicked++;
            }
            if (cc instanceof StarCabalCard) {
                starsPicked++;
            }
        }
        getDiscard().addAll(MyLists.filter(cardsPickedThisTurn, cc -> cc instanceof JunkCabalCard));
        cardsPickedThisTurn.clear();
    }

    private boolean gameIsOver() {
        return allStarsHaveBeenDrawn() || allDaggersHaveBeenDrawn();
    }

    public int getDaggersPicked() {
        int daggersThisTurn = MyLists.intAccumulate(cardsPickedThisTurn, cc -> cc instanceof DaggerCabalCard ? 1 : 0);
        return daggersPicked + daggersThisTurn;
    }

    private boolean allDaggersHaveBeenDrawn() {
         return getDaggersPicked() == 2;
    }

    public int getStarsPicked() {
        return starsPicked + MyLists.intAccumulate(cardsPickedThisTurn, cc -> cc instanceof StarCabalCard ? 1 : 0);
    }

    private boolean allStarsHaveBeenDrawn() {
        return  getStarsPicked() == getTotalStars();
    }

    private void recallAllCards() {
        for (CardGamePlayer p : getPlayers()) {
            while (p.numberOfCardsInHand() > 0) {
                getDeck().add(p.removeCard(0, this));
            }
        }
        Collections.shuffle(getDeck());
    }

    @Override
    public void foldPlayer(Model model, CardGameState state, CardGamePlayer player) {

    }

    @Override
    protected CardGamePlayer makeCharacterPlayer(GameCharacter leader, int obols) {
        return new CabalCharacterPlayer(leader, obols);
    }

    @Override
    public void doCardInHandAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {}

    @Override
    public void doOtherCardAction(Model model, CardGameState state, CardGamePlayer currentPlayer, CardGameCard cardGameCard) {}

    @Override
    public int getMaximumBet() {
        return MAX_BET;
    }

    @Override
    public void addMorePlayers(Model model, CardGameState cardGameState) {

    }

    @Override
    public void triggerTutorial(Model model) {
        model.getTutorial().cardGameCabal(model);
    }

    private static CardGamePlayer makeCabalNPC(Race r) {
        var gender = MyRandom.flipCoin();
        return new CabalNPC(GameState.randomFirstName(gender), gender, r);
    }

    public int getTurn() {
        return turn;
    }

    public int getPotSize() {
        return getCabalPlayers().size() * MAX_BET;
    }

    public void pickRandomCardFromPlayer(CardGameState state, CabalCardGamePlayer performer, CabalCardGamePlayer target) {
        MyLists.forEach(getCabalPlayers(), ccp -> ccp.updateSuspicionsForPick(this, performer, target));
        state.addHandAnimation(performer, false, false, false);
        CardGameCard card = target.getCard(MyRandom.randInt(target.numberOfCardsInHand()));
        target.removeCard(card, this);
        subView.addCardPlayedAnimation(target);
        state.waitUntil(subView, CabalCardGameSubView::playedCardAnimationIsDone);
        CabalCard cc = (CabalCard)card;
        state.println("It's a " + cc.getName() + ".");
        cc.commentOnCard(state, this, performer, target);
        cardsPickedThisTurn.add((CabalCard) card);
        pickPlayer = (CabalCardGamePlayer) target;
    }

    @Override
    public String getUnderText(Model model) {
        if (turn == 0) {
            return "Game Setup";
        }
        int picksRemaining = getPlayers().size() - cardsPickedThisTurn.size();
        String player = "";
        if (pickPlayer != null) {
            player = ", " + pickPlayer.getName() + "'s pick.";
        }
        return "Turn " + MyStrings.numberWord(turn) + ". Picks remaining: " + picksRemaining + player;
    }

    public List<CabalCard> getPickedCards() {
        return cardsPickedThisTurn;
    }

    public int previousStars() {
        return starsPicked;
    }

    public int previousDaggers() {
        return daggersPicked;
    }

    public boolean isShowStonesAndCards() {
        return showStones;
    }

    private void setShowStonesAndCards(boolean b) {
        this.showStones = b;
    }

    public boolean showBag() {
        return showBag;
    }

    public int getTotalStars() {
        if (getPlayers().size() == 5) {
            return 6;
        }
        return 7;
    }

    public void addSuspicionForAll(CardGamePlayer target, double suspicionToAdd) {
        MyLists.forEach(getCabalPlayers(), ccp -> ccp.addToSuspicion((CabalCardGamePlayer) target, suspicionToAdd));
    }

    public String getCurrentSuspicionFor(CardGamePlayer npc) {
        if (pickPlayer == null) {
            return "";
        }
        if (pickPlayer instanceof CabalCharacterPlayer) {
            return "";
        }
        return String.format("%4.1f", ((CabalNPC)pickPlayer).getSuspicionFor((CabalCardGamePlayer) npc));
    }

    public CabalCardGamePlayer getPickPlayer() {
        return pickPlayer;
    }
}
