package model.states.cardgames.cabal;

import model.Model;
import model.races.Race;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.CardGameState;
import model.states.cardgames.sleeves.CardGameSleeve;
import util.*;

import java.util.ArrayList;
import java.util.List;

public class CabalNPC extends CabalCardGamePlayer {
    private CabalAI cabalAI;
    private final CabalSuspicions suspicions = new CabalSuspicions();

    public CabalNPC(String name, boolean gender, Race r) {
        super(name, gender, r, CardGameSleeve.makeRandomSleeve(), MyRandom.randInt(CabalCardGame.MAX_BET, 90), true);
        this.cabalAI = new CabalFriendAI();
    }

    @Override
    public void takeTurn(Model model, CardGameState state, CardGame cardGame) {
        state.print("It's " + getName() + "'s turn. ");
        int delaySeconds = MyRandom.randInt(4);
        if (delaySeconds > 0) {
            state.println(getName() + " is thinking...");
            model.getLog().waitForAnimationToFinish();
            state.delay(1000 * (delaySeconds + 1));
            if (delaySeconds == 1) {
                List<CabalCardGamePlayer> yellers = MyLists.filter(((CabalCardGame)cardGame).getCabalPlayers(),
                        ccp -> ccp.hasClaim() && ccp.getClaimedStars() > 1 && ccp.getClaimedDaggers() == 0);
                if (!yellers.isEmpty() && MyRandom.flipCoin()) {
                    MyRandom.sample(yellers).sayLine(state, MyRandom.sample(
                            List.of("Pick me!",
                                    getName() + ", you can trust me.",
                                    "Come over here!",
                                    "I've got stars!")));
                }
                model.getLog().waitForAnimationToFinish();
                state.delay(1000);
            }
        } else {
            state.println("");
        }
        CabalCardGame cabal = (CabalCardGame)cardGame;
        List<CabalCardGamePlayer> candidates = super.getEligiblePlayers(cabal);
        CabalCardGamePlayer nextPick = cabalAI.findBestPick((CabalCardGame) cardGame, this, candidates, suspicions);
        sayLine(state, MyRandom.sample(List.of(
                "I'll take a card from " + nextPick.getName() + ".",
                nextPick.getName() + ", I'm drawing from you.",
                "I want one of " + nextPick.getName() + "'s cards.",
                nextPick.getName() + ", one of yours.")));
        cabal.pickRandomCardFromPlayer(state, this, nextPick);
        model.getLog().waitForAnimationToFinish();
    }

    @Override
    public void runStartOfGameHook(Model model, CardGameState cardGameState, CardGame cardGame) {
        if (isTraitor()) {
            cabalAI = new CabalTraitorAI();
        }
    }

    @Override
    public void makeStartOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame, int turn) {
        MyPair<Integer, Integer> claim = cabalAI.makeEarlyClaim(this, cabalCardGame, turn);
        if (claim == null) {
            state.println(getName() + " makes no claim.");
        } else {
            setClaim(claim.first, claim.second);
            StringBuilder claimText = new StringBuilder();
            claimText.append("I have ");
            if (claim.first > 0) {
                claimText.append(MyStrings.numberWord(claim.first)).append(" Star").append(claim.first > 1 ? "s" : "");
                if (claim.second > 0) {
                    claimText.append(" and ");
                }
            }
            if (claim.second > 0) {
                claimText.append(MyStrings.numberWord(claim.second)).append(" Dagger").append(claim.second > 1 ? "s" : "");
            } else if (claim.first == 0) {
                claimText.append("nothing");
            }
            claimText.append(".");
            sayLine(state, claimText.toString());

        }
    }

    @Override
    public void makeEndOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame, int turn) {
        MyPair<Integer, Integer> newClaim = cabalAI.makeLateClaim(this, cabalCardGame, turn);
        int changeInStars = 0;
        int changeInDaggers = 0;
        if (!hasClaim()) {
            if (newClaim == null) {
                return; // no change
            }
            changeInStars = newClaim.first;
            changeInDaggers = newClaim.second;
        } else {
            changeInStars = newClaim.first - getClaimedStars();
            changeInDaggers = newClaim.second - getClaimedDaggers();
        }
        if (changeInStars == 0 && changeInDaggers == 0) {
            return; // no change
        }

        if (changeInDaggers == 0) {
            sayLine(state, "I actually had " + MyStrings.numberWord(newClaim.first) + " star" + (newClaim.first == 1 ? "" : "s") + ".");
        } else if (changeInStars == 0) {
            if (newClaim.second == 1) {
                sayLine(state, "I had a dagger actually.");
            } else if (newClaim.second == 0) {
                sayLine(state, "I actually didn't have any daggers.");
            } else {
                sayLine(state, "I had " + MyStrings.numberWord(newClaim.second) + " daggers actually.");
            }
        } else {
            sayLine(state, "I had " + MyStrings.numberWord(newClaim.first) + " star" + (newClaim.first == 1 ? "" : "s") +
                    " and " +
                    MyStrings.numberWord(newClaim.second) + " dagger" + (newClaim.second == 1 ? "" : "s") +
                    " actually.");
        }
        setClaim(newClaim.first, newClaim.second);
    }

    @Override
    public void updateSuspicionsForStartOfTurnClaim(CardGameState state, CabalCardGame game) {
        giveSusForDaggerClinches(game, state, false);
        giveSusForStarOverflow(game, state, false);
        giveSusForStarUnderflow(game, state);
    }

    private void giveSusForStarUnderflow(CabalCardGame game, CardGameState state) {
        List<CabalCardGamePlayer> starClaimers = MyLists.filter(game.getCabalPlayers(),
                ccp -> ccp.hasClaim() && ccp.getClaimedStars() > 0);
        int starsClaimed = MyLists.intAccumulate(starClaimers, CabalCardGamePlayer::getClaimedStars);
        if (starsClaimed < game.getTotalStars() - game.getStarsPicked()) {
            if (game.getPickPlayer() == this) {
                int extra = game.getTotalStars() - game.getStarsPicked() - starsClaimed;
                sayLine(state, "Only " + MyStrings.numberWord(starsClaimed) + " stars claimed. " +
                        "We're missing " + MyStrings.numberWord(extra) + ". Who's hiding them?");
            }
            List<CabalCardGamePlayer> targets = MyLists.filter(game.getCabalPlayers(),
                    ccp -> !ccp.hasClaim() || (ccp.getClaimedStars() == 0 && ccp.getClaimedDaggers() == 0));
            System.out.println(getName() + " gave " + MyLists.commaAndJoin(targets, CardGamePlayer::getName) + " " +
                    CabalSuspicions.STAR_OVERFLOW + " sus for star underflow.");
            addNormalized(targets,
                    CabalSuspicions.STAR_UNDERFLOW);
        }
    }

    private void giveSusForStarOverflow(CabalCardGame game, CardGameState state, boolean extraForHardClinch) {
        List<CabalCardGamePlayer> starClaimers = MyLists.filter(game.getCabalPlayers(),
                ccp -> ccp.hasClaim() && ccp.getClaimedStars() > 0);
        int starsClaimed = MyLists.intAccumulate(starClaimers, CabalCardGamePlayer::getClaimedStars);
        if (starsClaimed > game.getTotalStars() - game.getStarsPicked()) {
            if (game.getPickPlayer() == this && !extraForHardClinch) {
                int extra = starsClaimed - game.getTotalStars() + game.getStarsPicked();
                sayLine(state, MyStrings.capitalize(MyStrings.numberWord(starsClaimed)) + " claimed stars? That's " +
                        MyStrings.numberWord(extra) + " too many. Who's lying?");
            }

            System.out.println(getName() + " gave " + MyLists.commaAndJoin(starClaimers, CardGamePlayer::getName) +
                    " " + CabalSuspicions.STAR_OVERFLOW + " sus for star overflow.");
            addNormalized(starClaimers, CabalSuspicions.STAR_OVERFLOW);
            for (CabalCardGamePlayer ccp : MyLists.filter(starClaimers, ccp -> ccp.getClaimedStars() > 2)) {
                double extra = CabalSuspicions.STAR_OVERFLOW * (ccp.getClaimedStars() - 2);
                System.out.println("   And " + ccp.getName() + " " + extra + " extra sus for many stars claimed.");
                suspicions.addFor(ccp, extra);
            }

            if (extraForHardClinch && starClaimers.contains(this)) {
                starClaimers.remove(this);
                MyPair<Integer, Integer> starsAndDaggers = CabalAI.countStarsAndDaggers(this);
                if (getClaimedStars() == starsAndDaggers.first) { // Not lying
                    int leftAfterMine = (game.getTotalStars() - game.getStarsPicked()) - starsAndDaggers.first;
                    if (leftAfterMine == 0) { // Others lying
                        System.out.println(getName() + " gave " + MyLists.commaAndJoin(starClaimers, CardGamePlayer::getName) +
                                " " + CabalSuspicions.LIAR_IN_MY_EYES + " for being a liar.");
                        addNormalized(starClaimers, CabalSuspicions.LIAR_IN_MY_EYES);
                        if (starClaimers.size() == 1) {
                            sayLine(state, starClaimers.getFirst().getName() + " is a liar!");
                        }
                    }
                } else if (starClaimers.size() == 1) { // is lying and other is not
                    System.out.println(getName() + " gave " + MyLists.commaAndJoin(starClaimers, CardGamePlayer::getName) +
                            " " + CabalSuspicions.TRUTH_TELLER_IN_MY_EYES + " for telling the truth.");
                    addNormalized(starClaimers, CabalSuspicions.TRUTH_TELLER_IN_MY_EYES);
                    if (starClaimers.size() == 1) {
                        sayLine(state, starClaimers.getFirst().getName() + " is a liar!");
                    }
                }
            }

        }
    }

    private void giveSusForDaggerClinches(CabalCardGame game, CardGameState state, boolean extraForHardClinch) {
        List<CabalCardGamePlayer> daggerClaimers = MyLists.filter(game.getCabalPlayers(),
                ccp -> ccp.hasClaim() && ccp.getClaimedDaggers() > 0);
        int daggersClaimed = MyLists.intAccumulate(daggerClaimers, CabalCardGamePlayer::getClaimedDaggers);
        if (daggersClaimed > 2 - game.getDaggersPicked()) {
            System.out.println(getName() + " gave " + MyLists.commaAndJoin(daggerClaimers, CardGamePlayer::getName) + " " +
                    CabalSuspicions.DAGGER_CLINCH + " sus for dagger clinching.");
            addNormalized(daggerClaimers, CabalSuspicions.DAGGER_CLINCH);

            if (extraForHardClinch && daggerClaimers.contains(this)) {
                daggerClaimers.remove(this);
                MyPair<Integer, Integer> starsAndDaggers = CabalAI.countStarsAndDaggers(this);
                if (starsAndDaggers.second == getClaimedDaggers()) { // Not lying
                    int daggersLeftAfterMine = (2 - game.getDaggersPicked()) - starsAndDaggers.second;
                    if (daggersLeftAfterMine == 0) {
                        System.out.println(getName() + " gave " + MyLists.commaAndJoin(daggerClaimers, CardGamePlayer::getName) +
                                " " + CabalSuspicions.LIAR_IN_MY_EYES + " for being a liar.");
                        addNormalized(daggerClaimers, CabalSuspicions.LIAR_IN_MY_EYES);
                        if (daggerClaimers.size() == 1) {
                            sayLine(state, daggerClaimers.getFirst().getName() + " is a liar!");
                        }
                    }
                } else if (daggerClaimers.size() == 1) { // is lying and other is not
                    System.out.println(getName() + " gave " + MyLists.commaAndJoin(daggerClaimers, CardGamePlayer::getName) +
                            " " + CabalSuspicions.TRUTH_TELLER_IN_MY_EYES + " for telling the truth.");
                    addNormalized(daggerClaimers, CabalSuspicions.TRUTH_TELLER_IN_MY_EYES);
                    if (daggerClaimers.size() == 1) {
                        sayLine(state, daggerClaimers.getFirst().getName() + " is a liar!");
                    }
                }
            }
        }
    }

    private void giveTrustForDaggerReveals(CabalCardGame game) {
        List<CabalCardGamePlayer> daggerClaimers = MyLists.filter(game.getCabalPlayers(),
                ccp -> ccp.hasClaim() && ccp.getClaimedDaggers() > 0 && !ccp.didDrawPickAtStartOfTurn());
        int daggersClaimed = MyLists.intAccumulate(daggerClaimers, CabalCardGamePlayer::getClaimedDaggers);
        if (daggersClaimed == 2 - game.getDaggersPicked()) { // No clinches or hidden daggers
            addNormalized(daggerClaimers, CabalSuspicions.DAGGER_REVEALER_PER_TURN * game.getTurn());
            System.out.println(getName() + " gave " + MyLists.commaAndJoin(daggerClaimers, CardGamePlayer::getName) +
                    " " + CabalSuspicions.DAGGER_REVEALER_PER_TURN + " for dagger revealing.");
        }
    }

    @Override
    public void updateSuspicionsForEndOfTurnClaim(CardGameState state, CabalCardGame game) {
        giveSusForDaggerClinches(game, state, true);
        giveTrustForDaggerReveals(game);
        giveSusForStarOverflow(game, state, true);
        giveSusForStarUnderflow(game, state);

        // Find hidden daggers:
        List<CabalCardGamePlayer> daggerClaimers = MyLists.filter(game.getCabalPlayers(),
                ccp -> ccp.hasClaim() && ccp.getClaimedDaggers() > 0);
        int daggersClaimed = MyLists.intAccumulate(daggerClaimers, CabalCardGamePlayer::getClaimedDaggers);
        if (daggersClaimed < 2 - game.getDaggersPicked()) {
            List<CabalCardGamePlayer> originalPickDrawers = MyLists.filter(game.getCabalPlayers(),
                    CabalCardGamePlayer::didDrawPickAtStartOfTurn);
            double sus = CabalSuspicions.HIDDEN_DAGGER;
            MyPair<Integer, Integer> starsAndDaggers = CabalAI.countStarsAndDaggers(this);
            if (starsAndDaggers.second == 0 && !isTraitor()) { // Did not hide anything.
                originalPickDrawers.remove(this);
            }
            addNormalized(originalPickDrawers, sus);
            System.out.println(getName() + " gave " + MyLists.commaAndJoin(originalPickDrawers, CardGamePlayer::getName) + " " +
                    sus + " sus for hidden daggers.");
        }
    }

    @Override
    public void updateSuspicionsForPick(CabalCardGame game, CardGamePlayer performer, CardGamePlayer target) {
        CabalCardGamePlayer pickedfrom = (CabalCardGamePlayer) target;
        int score = new CabalFriendPickRanker(suspicions, game).score(pickedfrom, suspicions.getFor(pickedfrom));
        double scale = 60*(2.5 - game.getTurn()/2.0);
        double change = -score / scale;
        System.out.println(getName() + " gave " + performer.getName() + " " + change +
                " sus for drawing from " + target.getName() + ".");
        suspicions.addFor((CabalCardGamePlayer) performer, change);
    }

    @Override
    public void addToSuspicion(CabalCardGamePlayer target, double suspicionToAdd) {
        suspicions.addFor(target, suspicionToAdd);
    }

    @Override
    public void drawTeamConclusions(CabalCardGame cabalCardGame) {
        System.out.println(getName() + " is drawing team conclusions.");
        List<CabalCardGamePlayer> players = new ArrayList<>(cabalCardGame.getCabalPlayers());
        if (!isTraitor()) {
            addToSuspicion(this, -50);
            System.out.println(getName() + " gives -50 sus to self");
        }
        System.out.println("Sorting by sus descending");
        players.sort((o1, o2) -> (int)(getSuspicionFor(o2) - getSuspicionFor(o1)));
        for (int i = 0; i < players.size(); ++i) {
            CabalCardGamePlayer target = players.get(i);
            System.out.println(target.getName() + " has " + getSuspicionFor(target) + " sus");
            if (i < 2) {
                System.out.println("Gets 50 more");
                addToSuspicion(target, 50);
            } else {
                System.out.println("Gets 50 less");
                addToSuspicion(target, -50);
            }
        }
    }

    private void addNormalized(List<CabalCardGamePlayer> targets, double value) {
        double totalSuspicionToSpread = value * targets.size();
        if (targets.contains(this)) {
            totalSuspicionToSpread += value; // Actually, this assumes NPC is telling the truth, should be - value if lying.
        }

        for (CabalCardGamePlayer ccp : targets) {
            suspicions.addFor(ccp, totalSuspicionToSpread / targets.size());
        }
    }

    public double getSuspicionFor(CabalCardGamePlayer target) {
        return suspicions.getFor(target);
    }
}
