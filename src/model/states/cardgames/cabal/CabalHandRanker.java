package model.states.cardgames.cabal;

import java.util.Comparator;

public abstract class CabalHandRanker implements Comparator<CabalCardGamePlayer> {

    public abstract int score(CabalCardGamePlayer player, double sus);

}
