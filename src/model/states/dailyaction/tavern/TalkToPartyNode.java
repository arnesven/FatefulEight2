package model.states.dailyaction.tavern;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.states.GameState;
import model.states.RecruitState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.PartyAttitudesDialog;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

import java.util.*;
import java.util.function.Predicate;

public class TalkToPartyNode extends DailyActionNode {
    public TalkToPartyNode() {
        super("Interact with party members");
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
                return null;
            }

            int selected = multipleOptionArrowMenu(model,24, 25, List.of(
                    "Talk", "Pay wages", "Dismiss somebody", "Check attitudes", "Cancel"));
            if (selected == 0) {
                talkToPartyMember(model);
            } else if (selected == 1) {
                payWages(model);
            } else if (selected == 2) {
                RecruitState recruitState = new RecruitState(model, List.of());
                recruitState.goToDismiss(model);
            } else if (selected == 3) {
                super.showPartyAttitudesSubView(model);
            }

            return null;
        }

        private void talkToPartyMember(Model model) {
            println("Which party member do you want to talk to?");
            GameCharacter gc = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            if (gc == model.getParty().getLeader()) {
                leaderSay(MyRandom.sample(List.of("Now what should we do next...",
                        "'" + gc.getName() + "'s Company', that sounds pretty good...",
                        "Perhaps we should get some new gear.",
                        "Things are going pretty good.",
                        "Hmm... Should we have a fun night at the tavern? Or maybe just another night in the tent.")));
            } else {
                int dieRoll = MyRandom.rollD6();
                if (dieRoll < 2) {
                    leaderSay(gc.getFirstName() + ", where are you from originally?");
                    if (gc.hasHomeTown()) {
                        partyMemberSay(gc, "Me? I'm from " + gc.getHomeTown(model).getPlaceName() + ".");
                    } else {
                        partyMemberSay(gc, "Uhm, I'm not really from anywhere. " +
                                "Just sort of drifted from place to place ever since I was a little " + boyOrGirl(gc.getGender()) + ". " +
                                "Never really belonged anywhere.");
                        leaderSay("Well you belong with us now.");
                        partyMemberSay(gc, "Thanks " + model.getParty().getLeader().getFirstName() + ".");
                    }
                } else if (dieRoll < 4) {
                    leaderSay("How are you doing " + gc.getFirstName() + "?");
                    if (gc.getEquipment().getTotalCost() < gc.getLevel() * 20) {
                        partyMemberSay(gc, "Well, I could do with some new equipment.");
                        leaderSay("I'll see what I can do.");
                    } else if (gc.getAttitude(model.getParty().getLeader()) < 0) {
                        partyMemberSay(gc, "Things could be better actually. I'm not sure I like your style of leadership.");
                        if (MyRandom.flipCoin()) {
                            partyMemberSay(gc, "Why don't you appoint somebody else as our leader?");
                        } else {
                            partyMemberSay(gc, "How about giving me a raise?");
                        }
                        leaderSay("Maybe.");
                    } else if (generalAttitude(model, gc) < 0) {
                        leaderSay("Anything I can do to make it better?");
                        if (MyRandom.flipCoin()) {
                            partyMemberSay(gc, "Maybe if we had some more success. I'm aching to go on a real quest.");
                        } else {
                            partyMemberSay(gc, "How about giving me a raise?");
                        }
                        leaderSay("I'll see what I can do.");
                    }
                } else {
                    leaderSay("What's on your mind " + gc.getFirstName() + "?");
                    Map<PersonalityTrait, List<String>> answers = PersonalityTrait.makeConversations(gc);

                    List<PersonalityTrait> traits = MyLists.filter(new ArrayList<>(answers.keySet()), gc::hasPersonality);
                    if (traits.isEmpty()) {
                        partyMemberSay(gc, "Not much. Maybe I'll have another drink.");
                        leaderSay("Just don't get carried away.");
                    } else {
                        Collections.shuffle(traits);
                        List<String> answer = answers.get(traits.get(0));
                        for (int i = 0; i < answer.size(); i++) {
                            partyMemberSay(gc, answer.get(i));
                            i++;
                            if (i < answer.size()) {
                                leaderSay(answer.get(i));
                            }
                        }
                    }
                }
            }
        }

        private int generalAttitude(Model model, GameCharacter gc) {
            List<Integer> atts = MyLists.transform(model.getParty().getPartyMembers(), gc::getAttitude);
            return MyLists.intAccumulate(atts, i -> i);
        }

        private void payWages(Model model) {
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
                }
            }
        }

        private void bribePartyMember(GameCharacter target, int bribe) {
            target.addToAttitude(getModel().getParty().getLeader(), bribe/2);
            if (target.hasPersonality(PersonalityTrait.rude)) {
                partyMemberSay(target, MyRandom.sample(List.of("It's about time!", "Finally, some appreciation.")));
            } else if (target.hasPersonality(PersonalityTrait.greedy)) {
                partyMemberSay(target, MyRandom.sample(List.of(bribe + " gold, that's it? Pitiful.", "More please!",
                        "I think " + (bribe * 2) + " would have been more suitable!")));
            } else if (target.hasPersonality(PersonalityTrait.generous)) {
                partyMemberSay(target, MyRandom.sample(List.of("Are you sure we can afford this?",
                        "Somebody else probably needs this more than me.")));
            } else{
                partyMemberSay(target, MyRandom.sample(List.of("Much appreciated!", "Thank you!",
                        "How kind!", "I deserved this.", "My fair share, I'm sure.",
                        "My wage? Okay.")));
            }
            getModel().getParty().addToGold(-bribe);
        }
    }
}
