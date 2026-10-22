package model.states.cardgames.cabal;


public class ObviousTraitorPickRanker extends CabalHandRanker {
    private final CabalSuspicions suspicions;
    private final CabalCardGame game;

    public ObviousTraitorPickRanker(CabalSuspicions suspicions, CabalCardGame game) {
        this.suspicions = suspicions;
        this.game = game;
    }

    @Override
    public int compare(CabalCardGamePlayer o1, CabalCardGamePlayer o2) {
        return score(o1, suspicions.getFor(o1)) - score(o2, suspicions.getFor(o2));
    }

    public int score(CabalCardGamePlayer player, double sus) {
        int stars = player.hasClaim() ? player.getClaimedStars() : 0;
        int daggers = player.hasClaim() ? player.getClaimedDaggers() : 0;
        // Last pick of turn 3. Don't give it away now.
        if (sus > 20 && game.getTurn() == 3 && game.getPickedCards().size() == game.getPlayers().size() - 1) {
            return 10000;
        }
        return -stars * 100 + daggers * 200 + (int)(sus * 10.0);
    }
}
