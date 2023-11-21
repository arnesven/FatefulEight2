package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.states.GameState;
import util.MyLists;
import util.MyRandom;
import view.PartyAttitudesDialog;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

import java.util.List;
import java.util.function.Predicate;

public class TalkToPartyNode extends DailyActionNode {
    public TalkToPartyNode() {
        super("Talk or pay wages to party members");
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
                print("To whom do you want to page wages? ");
                List<GameCharacter> others = MyLists.filter(model.getParty().getPartyMembers(),
                                Predicate.not(GameCharacter::isLeader));
                List<String> options = MyLists.transform(others, GameCharacter::getFirstName);
                options.add(0, "Nobody");
                if (others.size() > 1) {
                    options.add("Everybody");
                }
                int chosen = multipleOptionArrowMenu(model, 30, 20, options);
                if (chosen == 0) {
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(), List.of("You guys doing alright?",
                            "Ready to head out?", "How's it going?", "Everything OK over here?", "Enjoying the establishment?"));
                    super.showPartyAttitudesSubView(model);
                    GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    model.getParty().partyMemberSay(model, gc, List.of("Just having a brew...", "I'm loving it.3",
                            "Ready for action!", "Whenever you're ready boss.", "Ready to roll."));
                } else if (options.get(chosen).equals("Everybody")) {
                    if (model.getParty().getGold() < others.size()) {
                        println("You don't have enough money for that.");
                    } else {
                        int split = Math.min(10, model.getParty().getGold() / others.size());
                        print("Pay " + split + " gold to each party member (except the leader)? (Y/N) ");
                        if (yesNoInput()) {
                            MyLists.forEach(others, (GameCharacter gc) -> bribePartyMember(gc, split));
                        }
                        super.showPartyAttitudesSubView(model);
                    }
                } else {
                    if (model.getParty().getGold() < 2) {
                        println("You don't have enough money for that.");
                    } else {
                        int bribe = Math.min(model.getParty().getGold(), 10);
                        GameCharacter target = MyLists.find(others, (GameCharacter gc) ->
                                gc.getFirstName().equals(options.get(chosen)));
                        print("Pay " + bribe + " gold to " + target.getFirstName() + "? (Y/N) " );
                        if (yesNoInput()) {
                            bribePartyMember(target, bribe);
                        }
                        super.showPartyAttitudesSubView(model);
                    }
                }
            }
            return null;
        }

        private void bribePartyMember(GameCharacter target, int bribe) {
            target.addToAttitude(getModel().getParty().getLeader(), bribe/2);
            partyMemberSay(target, MyRandom.sample(List.of("Much appreciated!", "Thank you!",
                    "How kind!", "I deserved this.", "My fair share, I'm sure.",
                    "My wage? Okay.")));
            getModel().getParty().addToGold(-bribe);
        }
    }
}
