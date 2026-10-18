package model.states.cardgames.cabal;

import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class CabalTraitorAI extends CabalAI {
    private static final int[] TELL_TRUTH_RATIO_BY_TURN = new int[]{3, 8, 100, 10000};
    private static final int STARS_DAMAGE_CONTROL_RATIO = 5;
    private static final int COVER_STARS_WITH_DAGGER_RATIO = 3;
    private static final int ERROR_RATE = 15;

    @Override
    public MyPair<Integer, Integer> makeEarlyClaim(CabalNPC self, CabalCardGame game, int turn) {
        MyPair<Integer, Integer> starsAndDaggers = countStarsAndDaggers(self);
        int stars = starsAndDaggers.first;
        int daggers = starsAndDaggers.second;

        if (game.getTurn() == 4 && daggers == 0 && stars == 0 && self.getSuspicionFor(game.getPickPlayer()) > 10.0) {
            return new MyPair<>(0, 1);
        }

        if (MyRandom.randInt(TELL_TRUTH_RATIO_BY_TURN[turn-1]) == 0) {
            var answer = new CabalFriendAI().makeEarlyClaim(self, game, turn);
            if (answer != null && !(stars > 2 && answer.first > 2)) {
                return answer;
            }
        }

        if (daggers == 0 && stars == 0) {
            return new MyPair<>(1, 0);
        }

        if (daggers == 0) { // and stars > 0
            if (stars > 2 && MyRandom.randInt(STARS_DAMAGE_CONTROL_RATIO) == 0) {
                return new MyPair<>(1, 0);
            }
            if (stars > 1 && stars < 6 - turn && MyRandom.randInt(COVER_STARS_WITH_DAGGER_RATIO) == 0) {
                return new MyPair<>(stars, 1);
            }
            if (MyRandom.flipCoin()) {
                return new MyPair<>(0, 0);
            }
            return null;
        }

        if (turn == 4 && game.getDaggersPicked() == 0) {
            if (MyRandom.flipCoin()) {
                return new MyPair<>(0, 0);
            }
            return null;
        }

        if (stars == 0) { // and daggers > 0
            if (turn == 1) {
                return new MyPair<>(MyRandom.sample(List.of(1, 2, 2, 2, 3)), 0);
            }
            if (turn == 2) {
                return new MyPair<>(MyRandom.sample(List.of(1, 2)), 0);
            }
            return new MyPair<>(1, 0);
        }

        // stars > 0 && daggers > 0
        return new MyPair<>(Math.min(5 - turn, stars+1), 0);
    }

    @Override
    public MyPair<Integer, Integer> makeLateClaim(CabalNPC self, CabalCardGame game, int turn) {
        MyPair<Integer, Integer> starsAndDaggers = countStarsAndDaggers(self);
        if (starsAndDaggers.second > 0 && !self.didDrawPickAtStartOfTurn()) {
            return starsAndDaggers; // Try to appear good by confessing about daggers
        }
        if (!self.hasClaim()) {
            return null;
        }
        return new MyPair<>(self.getClaimedStars(), self.getClaimedDaggers());
    }

    @Override
    public CabalCardGamePlayer findBestPick(CabalCardGame game, CabalNPC self, List<CabalCardGamePlayer> candidates, CabalSuspicions suspicions) {
        List<CabalCardGamePlayer> cabalCandidates = MyLists.transform(candidates, cp -> (CabalCardGamePlayer) cp);
        CabalHandRanker ranker;
        if (suspicions.getFor(self) > CabalSuspicions.SOFT_REVEALED/2.0 || game.getTurn() == 4) {
            ranker = new ObviousTraitorPickRanker(suspicions, game);
        } else if (suspicions.getFor(self) > 5.0 || game.getTurn() == 3 ||
                (game.getTurn() == 2 && MyRandom.flipCoin()) ||
                (game.getTurn() == 1 && MyRandom.randInt(7) == 1)) {
            ranker = new DubiousPickRanker(suspicions, game);
        } else {
            ranker = new CabalFriendPickRanker(suspicions, game);
        }
        cabalCandidates.sort(ranker);
        List<Integer> scores = new ArrayList<>();
        for (CabalCardGamePlayer ccg : cabalCandidates) {
            int score = ranker.score(ccg, self.getSuspicionFor(ccg));
            System.out.println(ccg.getName() + ": " + score);
            scores.add(score);
        }
        int nextToLast = cabalCandidates.size() - 2;
        if (game.getTurn() < 4) {
            if (MyRandom.randInt(ERROR_RATE) == 0 && ((double)scores.get(nextToLast) / scores.getLast()) > 0.75) {
                System.out.println(self.getName() + " makes an error!");
                return cabalCandidates.get(nextToLast);
            }
        }
        return cabalCandidates.getLast();
    }
}
