package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.states.DailyEventState;
import model.states.RecruitState;
import model.states.ShopState;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.OtherPartySubView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherPartyEvent extends DailyEventState {
    private ArrayList<GameCharacter> otherPartyMembers;
    private GameCharacter leader;
    private HashMap<GameCharacter, Integer> attitudeMap;
    private HashMap<GameCharacter, Integer> attitudeTowardLeaderMap;
    private ShopState shop = null;
    private int baseAttitudeVsLeader;

    public OtherPartyEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You come upon a small campsite, this one seems to be in use by a group of people.");
        model.getParty().randomPartyMemberSay(model, List.of("Looks like a company of adventurers."));
        model.getParty().randomPartyMemberSay(model, List.of("The competition?"));
        leaderSay("Maybe. Or maybe not. Let's proceed with care.");

        OtherPartySubView subView = makeOtherPartySubView(model);
        model.setSubView(subView);
        print("You can trade with the other party, offer them something, attack them, talk to them, or leave.");
        do {
            waitForReturn();
            if (subView.getTopIndex() == 3) {
                break;
            }
            if (subView.getTopIndex() == 2) {
                println("Offer");
            } else if (subView.getTopIndex() == 1) {
                trade(model);
                CollapsingTransition.transition(model, subView);
            } else if (subView.getTopIndex() == 0) {
                println("Attack");
            } else {
                if (subView.getSelectedCharacter() == leader) {
                    talkToLeader(model);
                } else {
                    talkTo(model, subView.getSelectedCharacter());
                }
            }
        } while (true);
        println("You leve the camp site.");
        //int chosen = multipleOptionArrowMenu(model, 30, 30, List.of("Approach leader"));
    }

    private OtherPartySubView makeOtherPartySubView(Model model) {
        this.otherPartyMembers = new ArrayList<>();
        int level = MyRandom.randInt(2, 5);
        int baseAttitude = MyRandom.randInt(-35, Math.min(20, model.getParty().getReputation()*5));
        System.out.println("Base attitude of other party is: " + baseAttitude);
        int maxLeadership = 0;
        this.leader = null;
        this.attitudeMap = new HashMap<>();
        this.attitudeTowardLeaderMap = new HashMap<>();
        this.baseAttitudeVsLeader = MyRandom.randInt(-2, 2);
        for (int i = MyRandom.randInt(3, 8); i > 0; --i) {
            GameCharacter rando;
            while (true) {
                rando = MyRandom.sample(model.getAllCharacters());
                if (!otherPartyMembers.contains(rando)) {
                    otherPartyMembers.add(rando);
                    break;
                }
            }
            rando.setRandomStartingClass();
            rando.setLevel(MyRandom.randInt(level-1, level+2));
            int att = MyRandom.randInt(baseAttitude-5, baseAttitude+5);
            System.out.println("Attitude of other party member: " + att);
            attitudeMap.put(rando, att);
            attitudeTowardLeaderMap.put(rando, MyRandom.randInt(baseAttitudeVsLeader-1, baseAttitudeVsLeader+1));
            if (rando.getRankForSkill(Skill.Leadership) > maxLeadership) {
                maxLeadership = rando.getRankForSkill(Skill.Leadership);
                leader = rando;
            }
        }
        return new OtherPartySubView(otherPartyMembers, leader, attitudeMap);
    }


    private void trade(Model model) {
        if (shop == null) {
            println(leader.getFullName() + ": \"We have a few things that we can bare to part with. " +
                    "For the right price of course.\"");
            List<Item> otherInventory = new ArrayList<>(model.getItemDeck().draw(MyRandom.randInt(6, 12)));
            int[] prices = new int[otherInventory.size()];
            int i = 0;
            for (Item it : otherInventory) {
                prices[i++] = (int)Math.ceil(it.getCost() * (1.0 - (attitudeMap.get(leader) / 100.0)));
            }
            this.shop = new ShopState(model, "OTHER PARTY", otherInventory, prices);
        }
        this.shop.run(model);
    }

    private void talkToLeader(Model model) {
        int chosen = multipleOptionArrowMenu(model, 24, 32,
                List.of("Ask about recent success",
                        "Propose to merge parties"
                ));
        if (chosen == 0) {
            String reply;
            if (baseAttitudeVsLeader == -2) {
                reply = "Well, it's going pretty poorly if I say so myself. I don't think my party members really " +
                        "want me as their leader anymore.";
            } else if (baseAttitudeVsLeader == -1) {
                reply = "Not great. We've had a string of bad luck, and I've been seeing some sour faces recently.";
            } else if (baseAttitudeVsLeader == 0) {
                reply = "I say we are about average. We've done a few jobs recently, but they haven't generated " +
                        "much coin. I'm happy I've been able to keep my party members fed.";
            } else if (baseAttitudeVsLeader == 1) {
                reply = "Pretty good. I've managed to keep my party members fed, equipped and happy.";
            } else {
                reply = "Excellent. We've had some good luck recently, and I managed to invest that well. We're now " +
                        "reaping the benefits of my good decisions.";
            }
            println(leader.getName() + ": \"" + reply + "\"");
        } else {
            // TODO: Perhaps merge?
        }
    }

    private void talkTo(Model model, GameCharacter who) {
        println("You approach " + who.getFullName() + ".");
        int chosen = multipleOptionArrowMenu(model, 22, 32,
                List.of("Ask about feelings toward party",
                        "Ask if " + (heOrShe(who.getGender())) + " wants to join"
                ));

        if (chosen == 0) {
            String reply;
            if (attitudeMap.get(who) < -10) {
                reply = MyRandom.sample(List.of("No comment.", "I don't trust you enough to talk about that.",
                        "I don't want to talk about that."));
            } else {
                reply = getReply(attitudeTowardLeaderMap.get(who));
            }
            println(who.getFullName() + ": \"" + reply + "\"");
            attitudeMap.put(leader, attitudeMap.get(leader) - 1);
        } else {
            int baseDifficulty = 11 + attitudeTowardLeaderMap.get(who) * 3 -
                    ((int)Math.round(attitudeMap.get(who) / 5.0));
            SkillCheckResult result = model.getParty().getLeader().testSkill(Skill.Persuade, baseDifficulty);
            println(model.getParty().getLeader().getName() + " attempts to persuade " + who.getName() + " to join the " +
                    "party (Persuade " + result.asString() + ").");
            if (result.isSuccessful()) {
                attitudeMap.put(leader, attitudeMap.get(leader) - 20);
                RecruitState state = new RecruitState(model, List.of(who));
                state.run(model);
                println(leader.getFirstName() + " is furious with you!");
            } else {
                attitudeMap.put(leader, attitudeMap.get(leader) - 5);
                println(who.getName() + " declines to join your party.");
                println(leader.getFirstName() + " is visibly irritated by your attempt at recruiting one of the " +
                        "party members.");
            }
        }
    }

    private String getReply(int attitude) {
        switch (attitude) {
            case -3:
                return MyRandom.sample(List.of(
                        "Honestly, it's been horrible! Ever since " + leader.getFirstName() +
                                " took over we've been doing nothing but roaming endlessly in the wilds. Sometimes we even " +
                                "have to starve because " + heOrShe(leader.getGender()) + " didn't plan properly. I'm sick of this",
                        "Oh, it's pretty terrible. " + leader.getFirstName() + " is an awful leader who freezes up under pressure " +
                                "or makes horribly bad decisions. I've been thinking about leaving for some time now.",
                        "Things are really bad. We lost one party member in our last fight, and I wouldn't have made it " +
                                "unless I hadn't disobeyed one of " + leader.getFirstName() + "'s idiotic commands. I'm sure just " +
                                "about anybody could manage a party better than " + heOrShe(leader.getGender()) + " could.",
                        leader.getFirstName() + " is an incompetent fool. We just got our rear ends handed to us by a bunch of thugs " +
                                "who could have easily been persuaded to leave us alone for a few gold coins."));

            case -2:
                return MyRandom.sample(List.of(
                        "Well, things have been better, I can tell you. Recently we were contracted for a quest, but " +
                                leader.getFirstName() + " made some bad calls and we had to pull out, so we never got our payday. " +
                                "Now our prospects look pretty bleak. Our rations will run out in a week, I wonder what we'll do then.",
                        "Not great. I don't want to get into any details, but I had an argument with " + leader.getFirstName() + " recently " +
                                "and, well let's just say it didn't end very well.",
                        "I think there are probably better leaders than " + leader.getFirstName() + ". We haven't had much success out here.",
                        "I feel like I have a connection to some of the party members, but I just can't say that I trust " + leader.getFirstName() + "."
                ));

            case -1:
                return MyRandom.sample(List.of(
                        "Things could have been better. We're not as equipped as I think we ought to be. " + leader.getFirstName() +
                        " is holding on to the money for some reason. Maybe " + heOrShe(leader.getGender()) + " is just being greedy?",
                        leader.getFirstName() + " is okay, I mean, everybody makes mistakes right? I think our luck will turn soon.",
                        "It feels like we haven't had a proper job in ages. I wish we could stay closer to urban areas, or perhaps go visit a castle? " +
                        "I feel like sometimes I just don't understand " + leader.getFirstName() + "'s priorities."
                ));

            case 0:
                return MyRandom.sample(List.of(
                        "My feelings on the matter are mixed. Some days I feel like this party is going nowhere, then all of the sudden " +
                                "we see some coin coming in and we head to the tavern. All things considered, it's all right.",
                        leader.getFirstName() + "'s style of leadership is... well different. It's not without merit, but sometimes " +
                                "I wonder if there aren't better ways of achieving success. But what do I know?",
                        "I haven't got much to say on the subject. Some days are good, other's not so much.",
                        "Eh? Things are okay I guess."
                ));

            case 1:
                return MyRandom.sample(List.of(
                        "Pretty good, I'd say. We've had a moderate amount of success and I feel like I can trust " + leader.getFirstName() + ".",
                        "I feel at home in the party. We have a good thing going here. Leadership is alright.",
                        "I'm happy. I'm sure we're not the fiercest gang out there, but we find jobs and manage to do them well.",
                        "It's alright. " + leader.getFirstName() + " is a good leader."
                ));

            case 2:
                return MyRandom.sample(List.of(
                        "Surprisingly good I must say. I was sceptical when first I joined, but now I feel very confident " +
                        "about the party's capability. " + leader.getFirstName() + "? " + heOrSheCap(leader.getGender()) +
                                "'s a competent leader.",
                        "Great! We recently cleared out a dungeon. It was tough, but we did it. We're lucky " + leader.getFirstName() +
                        " knows " + hisOrHer(leader.getGender()) + " stuff.",
                        "I have a good feeling about the party. I have friends here. I feel like my skills are appreciated and " +
                        "the pay is what I deserve."
                ));

            case 3:
                return MyRandom.sample(List.of(
                        "Very good indeed. I trust " + leader.getFirstName() + ", and the other members of the party with my life.",
                        "We've had some great success recently. We've have been on many quests, and with " + leader.getFirstName() +
                        " great leadership, and the rest of our team's good skills, we've always come out on top.",
                        "I wouldn't leave this group for another, we've got a very good thing going here."
                ));
        }
        throw new IllegalStateException("Illegal other party member attitude toward their leader " + attitude);
    }
}
