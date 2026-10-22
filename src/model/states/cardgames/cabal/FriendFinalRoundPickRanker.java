package model.states.cardgames.cabal;

import util.MyLists;
import util.MyPair;

import java.util.List;

public class FriendFinalRoundPickRanker extends CabalHandRanker {
    private final CabalSuspicions suspicions;
    private final CabalNPC self;
    private final CabalCardGame game;

    public FriendFinalRoundPickRanker(CabalCardGame game, CabalNPC self, CabalSuspicions suspicions) {
        this.game = game;
        this.self = self;
        this.suspicions = suspicions;
    }

    @Override
    public int compare(CabalCardGamePlayer o1, CabalCardGamePlayer o2) {
        return score(o1, suspicions.getFor(o1)) - score(o2, suspicions.getFor(o2));
    }

    public int score(CabalCardGamePlayer player, double sus) {
        MyPair<Integer, Integer> starsAndDaggers = CabalAI.countStarsAndDaggers(self);
        int daggers = player.hasClaim() ? player.getClaimedDaggers() : 0;
        if (starsAndDaggers.first == game.getTotalStars() - game.getStarsPicked() ||  // self has all remaining stars
            starsAndDaggers.first > 0 && badGuysHaveRemainingStars(game)) {
            return (int)(-1.0 * sus) - daggers * 500; // Just bounce off of most trusted player who doesn't have dagger
        }

        int stars = player.hasClaim() ? player.getClaimedStars() : 0;
        if (sus < 2.0) { // Assume player is friend
            return stars * 100 - daggers * 90;
        }
        // Player is traitor
        return 5 + (int)(sus / 25.0);
    }

    private boolean badGuysHaveRemainingStars(CabalCardGame game) {
        List<CabalCardGamePlayer> goodGuys = MyLists.filter(game.getCabalPlayers(),
                cc -> self.getSuspicionFor(cc) < -5.0);
        return MyLists.all(goodGuys, cc -> !cc.hasClaim() || cc.getClaimedStars() == 0);
    }
}
