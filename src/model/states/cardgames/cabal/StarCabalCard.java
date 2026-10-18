package model.states.cardgames.cabal;

import model.states.cardgames.CardGameState;
import util.MyRandom;
import view.MyColors;

import java.util.List;

public class StarCabalCard extends CabalCard {
    public StarCabalCard() {
        super(10, MyColors.GREEN, "Star");
    }

    @Override
    public void commentOnCard(CardGameState state, CabalCardGame game, CabalCardGamePlayer performer, CabalCardGamePlayer target) {
        if (!target.hasClaim()) {
            performer.sayLine(state, MyRandom.sample(List.of("Look what I found!",
                    "You didn't care to tell us about that?")));
            target.sayLine(state, "I had my reasons.");
            game.addSuspicionForAll(target, CabalSuspicions.STAR_UNDERFLOW * game.getTurn());
        } else if (target.getClaimedStars() == 0) {
            performer.sayLine(state, MyRandom.sample(List.of("Hey, I thought you didn't have any!", "Oh, a star?")));
            target.sayLine(state, "Oh right... that one.");
            game.addSuspicionForAll(target, (CabalSuspicions.UNCLAIMED_STAR_PICKED * game.getTurn())/2.0);
        } else if (target.getClaimedDaggers() > 0) {
            if (target instanceof CabalNPC && ((CabalNPC)target).getSuspicionFor(target) > 45) {
                target.sayLine(state, MyRandom.sample(List.of("Too bad.", "Almost.", "Aaw")));
            } else if (game.getTurn() < 4) {
                target.sayLine(state, MyRandom.sample(List.of("Wait, what?", "But I told you...", "Risky!")));
            }
        } else {
            if (performer instanceof CabalNPC && ((CabalNPC)performer).getSuspicionFor(performer) > 45) {
                performer.sayLine(state, MyRandom.sample(List.of("Bah.", "Shoot!", "Darn it.", "Doh!")));
            } else {
                performer.sayLine(state, MyRandom.sample(List.of("Yes!", "Alright!", "Good!", "Great!",
                        "Ah, ah star!", "Finally.")));
                    if (target instanceof CabalNPC && ((CabalNPC)target).getSuspicionFor(target) < 5.0) {
                        target.sayLine(state, MyRandom.sample(List.of("See?", "Good on my word.", "Thanks for trusting me.")));
                    }
                }
            target.setClaim(target.getClaimedStars() - 1, target.getClaimedDaggers());
            game.addSuspicionForAll(target, (CabalSuspicions.CLAIMED_STAR_PICKED * game.getTurn() / 2.0));
        }
    }
}
