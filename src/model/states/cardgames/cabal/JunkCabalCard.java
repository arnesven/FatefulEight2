package model.states.cardgames.cabal;

import model.states.cardgames.CardGameState;

import util.MyRandom;
import view.MyColors;

import java.util.List;


public class JunkCabalCard extends CabalCard {
    public JunkCabalCard() {
        super(12, MyColors.BROWN, "Junk");
    }

    @Override
    public void commentOnCard(CardGameState state, CabalCardGame game, CabalCardGamePlayer performer, CabalCardGamePlayer target) {
        if (target.hasClaim() && target.getClaimedStars() > target.numberOfCardsInHand()) {
            performer.sayLine(state, "Uhm, " + target.getName() + "... I expected a star.");
            target.sayLine(state, "I inflated my claim a bit, sorry.");
            game.addSuspicionForAll(target, CabalSuspicions.MISSING_CLAIMED_STAR * game.getTurn());
        }
        if (target.hasClaim() && target.getClaimedDaggers() > 0) {
            if (target instanceof CabalNPC && ((CabalNPC)target).getSuspicionFor(target) > 45) {
                target.sayLine(state, MyRandom.sample(List.of("Too bad.", "Almost.", "Aaw")));
            } else if (game.getTurn() < 4) {
                target.sayLine(state, MyRandom.sample(List.of("Unwise",
                        "What are you doing!?", "Traitor!", "Wait, what?", "No!",
                        performer.getName() + "...")));
            }
        }
    }
}
