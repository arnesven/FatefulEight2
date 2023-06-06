package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.states.GameState;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

import java.util.List;

public class TalkToPartyNode extends DailyActionNode {
    public TalkToPartyNode() {
        super("Talk to party members");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TalkToPartyState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }

    private static class TalkToPartyState extends GameState {
        public TalkToPartyState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            if (model.getParty().size() == 1) {
                model.getParty().randomPartyMemberSay(model, List.of("It's just me for now.",
                        "I better find some comrades if I'm going to take on the world.",
                        "Nope, nobody here yet.", "I'm kind of lonely."));
            } else {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(), List.of("You guys doing alright?",
                        "Ready to head out?", "How's it going?", "Everything OK over here?", "Enjoying the establishment?"));
                GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                model.getParty().partyMemberSay(model, gc, List.of("Just having a brew...", "I'm loving it.3",
                        "Ready for action!", "Whenever you're ready boss.", "Ready to roll."));
            }
            return null;
        }
    }
}
