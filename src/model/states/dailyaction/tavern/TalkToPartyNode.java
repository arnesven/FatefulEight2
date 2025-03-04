package model.states.dailyaction.tavern;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.states.GameState;
import model.states.PayWagesState;
import model.states.RecruitState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import util.MyLists;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

import java.util.*;

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
                new PayWagesState(model).explicitPayWages(model);
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
    }
}
