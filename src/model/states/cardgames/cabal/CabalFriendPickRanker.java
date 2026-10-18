package model.states.cardgames.cabal;

import util.MyLists;

import java.util.List;

public class CabalFriendPickRanker extends CabalHandRanker {
    private final CabalSuspicions suspicions;
    private final CabalCardGame game;

    public CabalFriendPickRanker(CabalSuspicions suspicions, CabalCardGame game) {
        this.suspicions = suspicions;
        this.game = game;
    }

    @Override
    public int compare(CabalCardGamePlayer o1, CabalCardGamePlayer o2) {
        return score(o1, suspicions.getFor(o1)) - score(o2, suspicions.getFor(o2));
    }

    public int score(CabalCardGamePlayer player, double sus) {
        if (sus < -5.0 && (!player.hasClaim() || player.getClaimedStars() == 0)) {
            return -200;
        }
        int stars = player.hasClaim() ? player.getClaimedStars() : 0;
        int daggers = player.hasClaim() ? player.getClaimedDaggers() : 0;
        List<CabalCardGamePlayer> starClaimers = MyLists.filter(game.getCabalPlayers(),
                ccp -> ccp.hasClaim() && ccp.getClaimedStars() > 0);
        int starsClaimed = MyLists.intAccumulate(starClaimers, CabalCardGamePlayer::getClaimedStars);
        int missingStars = game.getTotalStars() - game.getStarsPicked() - starsClaimed;

        // Last pick of turn 3, don't give it away to other team.
        if (sus > 20 &&  game.getTurn() == 3 && game.getPickedCards().size() == game.getPlayers().size() - 1) {
            return -600;
        }
        if (sus > 6.0 && stars == 0 && daggers == 0 && missingStars > 0 &&
                game.getPickedCards().size() < game.getPlayers().size() - 1) {
            return 25 * missingStars;
        }
        return stars * 100 - daggers * 1000 - (int)(sus * 20.0);
    }
}
