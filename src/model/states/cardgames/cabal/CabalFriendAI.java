package model.states.cardgames.cabal;

import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class CabalFriendAI extends CabalAI {

    private static final int[] INFLATE_STAR_RATIO_BY_TURN = new int[]{12, 25, 500, 10000};
    private static final int INFLATE_DAGGER_RATIO_TURN_1 = 20;
    private static final int ERROR_RATE = 10;
    private static final int COVER_NOTHING_WITH_DAGGER_RATIO = 7;

    @Override
    public MyPair<Integer, Integer> makeEarlyClaim(CabalNPC self, CabalCardGame game, int turn) {
        MyPair<Integer, Integer> starsAndDaggers = countStarsAndDaggers(self);
        int stars = starsAndDaggers.first;
        int daggers = starsAndDaggers.second;

        if (game.getTurn() == 4 && daggers == 1 && stars == 1) {
            return new MyPair<>(1, 1);
        }

        if (daggers == 0 && stars == 0) {
            if (MyRandom.randInt(turn + 1) != 0 || game.getTurn() == 4) {
                return new MyPair<>(0, 0);
            }

            if (MyRandom.randInt(COVER_NOTHING_WITH_DAGGER_RATIO) == 0 && game.getDaggersPicked() < 2) {
                return new MyPair<>(0, 1);
            }
            return null;
        }

        if (daggers == 0) { // and stars > 0
            int max = 6 - turn;
            return new MyPair<>(stars + Math.min(max, MyRandom.randInt(INFLATE_STAR_RATIO_BY_TURN[turn-1]) == 0 ? 1 : 0),
                    0);
        }

        if (stars == 0) { // and daggers > 0
            if (turn == 1) {
                return new MyPair<>(0, daggers + MyRandom.randInt(INFLATE_DAGGER_RATIO_TURN_1) == 0 ? 1 : 0);
            }
            if (turn == 2 && MyRandom.flipCoin()) {
                return new MyPair<>(0, daggers);
            }
            if (turn > 2 && game.getDaggersPicked() > 0) {
                return new MyPair<>(0, daggers);
            }
            return null;
        }

        // stars > 0 and daggers > 0
        if (stars > daggers || turn == 1) {
            return new MyPair<>(stars, daggers);
        }

        // stars == daggers or daggers > stars
        return null;
    }

    @Override
    public MyPair<Integer, Integer> makeLateClaim(CabalNPC self, CabalCardGame game, int turn) {
        return countStarsAndDaggers(self);
    }

    @Override
    public CabalCardGamePlayer findBestPick(CabalCardGame game, CabalNPC friend, List<CabalCardGamePlayer> cabalCandidates, CabalSuspicions suspicions) {
        CabalHandRanker ranker;
        System.out.println("Ranking hands for " + friend.getName());
        if (game.getTurn() == 4) {
            ranker = new FriendFinalRoundPickRanker(game, friend, suspicions);
            System.out.println("Ranking by FriendFinalRoundPicker");
        } else {
            ranker = new CabalFriendPickRanker(suspicions, game);
            System.out.println("Ranking by CabalFriendPickRanker");
        }
        cabalCandidates.sort(ranker);
        List<Integer> scores = new ArrayList<>();
        for (CabalCardGamePlayer ccg : cabalCandidates) {
            int score = ranker.score(ccg, friend.getSuspicionFor(ccg));
            System.out.println(ccg.getName() + ": " + score);
            scores.add(score);
        }
        int nextToLast = cabalCandidates.size() - 2;
        if (game.getTurn() < 4) {
            if (MyRandom.randInt(ERROR_RATE) == 0 && ((double)scores.get(nextToLast) / scores.getLast()) > 0.75) {
                System.out.println(friend.getName() + " makes an error!");
                return cabalCandidates.get(nextToLast);
            }
        }
        return cabalCandidates.getLast();
    }
}
