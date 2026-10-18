package model.states.cardgames.cabal;

import java.util.HashMap;

public class CabalSuspicions extends HashMap<CabalCardGamePlayer, Double> {

    public static final double DAGGER_CLINCH = 10.0;
    public static final double STAR_OVERFLOW = 3.0;
    public static final double STAR_UNDERFLOW = 5.0;
    public static final double SOFT_REVEALED = 50.0;
    public static final double MISSING_CLAIMED_STAR = 15.0;
    public static final double UNCLAIMED_STAR_PICKED = 20.0;
    public static final double CLAIMED_STAR_PICKED = -8.0;
    public static final double HIDDEN_DAGGER = DAGGER_CLINCH;
    public static final double LIAR_IN_MY_EYES = 35.0;
    public static final double TRUTH_TELLER_IN_MY_EYES = -LIAR_IN_MY_EYES;
    public static final double DAGGER_REVEALER_PER_TURN = -8.0;

    public void addFor(CabalCardGamePlayer target, double change) {
        if (!containsKey(target)) {
            put(target, 0.0);
        }
        put(target, get(target) + change);
    }

    public double getFor(CabalCardGamePlayer target) {
        if (!containsKey(target)) {
            return 0.0;
        }
        return get(target);
    }
}
