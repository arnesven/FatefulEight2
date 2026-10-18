package model.states.cardgames.cabal;

import model.states.cardgames.CardGameState;
import util.MyRandom;
import view.MyColors;

import java.util.List;

public class DaggerCabalCard extends CabalCard {
    public DaggerCabalCard() {
        super(11, MyColors.RED, "Dagger");
    }

    @Override
    public void commentOnCard(CardGameState state, CabalCardGame game, CabalCardGamePlayer performer, CabalCardGamePlayer target) {
        performer.sayLine(state, MyRandom.sample(List.of("Shoot!", "Uh-oh.", "Oh no.", "Darn it!")));
        if (!target.hasClaim()) {
            performer.sayLine(state, "Why didn't you say anything?");
            target.sayLine(state, MyRandom.sample(List.of(
                    "Oh yeah, I have a Dagger. Or I had.",
                    "Uh... I guess I forgot to tell you about that.",
                    "I didn't want to tip off the traitors!")));
        } else if (target.getClaimedDaggers() == 0) {
            if (target.getClaimedStars() == 0) {
                performer.sayLine(state, "You said you didn't have any daggers!");
                target.sayLine(state, "I didn't think anybody would pick me!");
            } else {
                performer.sayLine(state, MyRandom.sample(List.of(target.getName() + "!",
                        "Oh come on " + target.getName() + ".",
                        target.getName() + ", you traitor!",
                        "I've should have known!")));
                target.sayLine(state, MyRandom.sample(List.of(
                        "I didn't think you'd believe me if I told you.",
                        "I made a mistake. Can you forgive me?",
                        "Hehehe...", "Gotcha!", "Trololololoo!")));
                game.addSuspicionForAll(target, CabalSuspicions.SOFT_REVEALED);
            }
        } else if (target.getClaimedDaggers() > 0) {
            target.sayLine(state, MyRandom.sample(List.of("I warned you.",
                    "I told you I had one.", "That's what I said.")));
            target.setClaim(target.getClaimedStars(), target.getClaimedDaggers() - 1);
        }
    }
}
