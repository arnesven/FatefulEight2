package model.states.cardgames.cabal;

import model.races.Race;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.CardGameState;
import model.states.cardgames.sleeves.CardGameSleeve;
import util.MyLists;

import java.util.List;

public abstract class CabalCardGamePlayer extends CardGamePlayer {

    enum Loyalty {unset, friend, traitor};

    static class Claim {
        public int stars;
        public int daggers;
        public Claim(int s, int d) {
            stars = s;
            daggers = d;
        }
    }

    private Loyalty loyalty = Loyalty.unset;
    private Claim originalClaim = null;
    private Claim claim = null;

    public CabalCardGamePlayer(String firstName, boolean gender, Race race, CardGameSleeve sleeve, int obols, boolean isNPC) {
        super(firstName, gender, race, sleeve, obols, isNPC);
    }

    public abstract void makeStartOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame, int turn);
    public abstract void makeEndOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame, int turn);
    public abstract void updateSuspicionsForStartOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame);
    public abstract void updateSuspicionsForEndOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame);
    public abstract void updateSuspicionsForPick(CabalCardGame cabalCardGame, CardGamePlayer performer, CardGamePlayer target);
    public abstract void addToSuspicion(CabalCardGamePlayer target, double suspicionToAdd);
    public abstract void drawTeamConclusions(CabalCardGame cabalCardGame);

    public void setTraitor(boolean isTraitor) {
        this.loyalty = isTraitor ? Loyalty.traitor : Loyalty.friend;
    }

    public boolean isTraitor() {
        return loyalty == Loyalty.traitor;
    }

    public boolean isLoyaltySet() {
        return loyalty != Loyalty.unset;
    }

    public void setClaim(int stars, int daggers) {
        if (claim != null) {
            this.originalClaim = new Claim(claim.stars, claim.daggers);
        } else {
            this.originalClaim = new Claim(stars, daggers);
        }
        this.claim = new Claim(stars, daggers);
    }

    public void clearClaim() {
        this.originalClaim = null;
        this.claim = null;
    }

    public boolean hasClaim() {
        return this.claim != null;
    }

    public int getClaimedStars() {
        return this.claim.stars;
    }

    public int getClaimedDaggers() {
        return this.claim.daggers;
    }

    protected static int claimInput(CardGameState state, int max, String what) {
        state.print("How many " + what + " do you want to claim: ");
        int x;
        do {
            String line = state.lineInput();
            try {
                x = Integer.parseInt(line);
                if (x < 0) {
                    state.println("You cannot claim a negative number of " + what + ".");
                } else if (x > max) {
                    state.println("You can at most claim " + max + " " + what + ".");
                } else {
                    break;
                }
            } catch (NumberFormatException nfe) {
                state.println("Please enter an integer between 0 and " + max + ".");
            }
        } while (true);
        return x;
    }

    protected List<CabalCardGamePlayer> getEligiblePlayers(CabalCardGame cardGame) {
        return MyLists.filter(cardGame.getCabalPlayers(),
                cardGamePlayer -> cardGamePlayer != this && cardGamePlayer.numberOfCardsInHand() > 0);
    }

    protected boolean didDrawPickAtStartOfTurn() {
        return originalClaim != null && originalClaim.stars > 0 && originalClaim.daggers == 0;
    }
}
