package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.*;
import model.classes.normal.BardClass;
import model.classes.normal.MagicianClass;
import model.classes.normal.WizardClass;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.horses.HorseHandler;
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
    private OtherPartySubView subView;
    private int otherPartyLevel;
    private boolean warningIssued = false;
    private boolean cookedMeal = false;

    public OtherPartyEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You come upon a small campsite, this one seems to be in use by a group of people.");
        model.getParty().randomPartyMemberSay(model, List.of("Looks like a company of adventurers."));
        model.getParty().randomPartyMemberSay(model, List.of("The competition?"));
        leaderSay("Maybe. Or maybe not. Let's proceed with care.");

        this.subView = makeOtherPartySubView(model);
        model.setSubView(subView);
        print("You can trade with the other party, offer them something, attack them, talk to them, or leave.");
        model.getTutorial().otherParties(model);
        do {
            waitForReturn();
            if (subView.getTopIndex() == 3) {
                break;
            }
            if (subView.getTopIndex() == 2) {
                offer(model);
            } else if (subView.getTopIndex() == 1) {
                trade(model);
                CollapsingTransition.transition(model, subView);
            } else if (subView.getTopIndex() == 0) {
                otherLeaderSay("You want to fight? Fine! Come on gang, let's teach them a lesson!");
                doAttack(model);
                break;
            } else {
                if (subView.getSelectedCharacter() == leader) {
                    if (talkToLeader(model)) {
                        break;
                    }
                } else {
                    talkTo(model, subView.getSelectedCharacter());
                }
            }
            if (attitudeMap.get(leader) <= -20) {
                if (warningIssued) {
                    otherLeaderSay("That's it. I think you need to be taught a lesson!");
                    doAttack(model);
                    break;
                } else {
                    otherLeaderSay("I'm warning you, stop putting your nose where it doesn't belong!#");
                    this.warningIssued = true;
                }
            }
        } while (true);
        println("You leve the camp site.");
    }

    private void offer(Model model) {
        List<String> options = new ArrayList<>(List.of("Offer gold", "Offer rations", "Offer entertainment"));
        if (model.getParty().getFood() > otherPartyMembers.size() && !cookedMeal) {
            options.add("Offer cooked meal");
        }

        int chosen = multipleOptionArrowMenu(model, 24, 31, options);
        if (chosen < 2) {
            offerGoldOrRations(model, chosen == 0);
        } else if (chosen == 2) {
            offerEntertainment(model);
        } else {
            cookedMeal = true;
            offerCookedMeal(model);
        }
    }

    private void offerEntertainment(Model model) {
        print("Who should perform the entertainment?");
        GameCharacter entertainer = model.getParty().partyMemberInput(model, this,
                model.getParty().getPartyMember(0));
        if (entertainer.getCharClass() instanceof BardClass) {
            println(entertainer.getFirstName() + " has offered to sing a ballad for the party.");
        } else if (entertainer.getCharClass() instanceof MagicianClass || entertainer.getCharClass() instanceof WizardClass) {
            println(entertainer.getFirstName() + " has offered to do a magic trick for the party.");
        } else {
            println(entertainer.getFirstName() + " has offered to tell a grand tale for the party.");
        }

        int entertainmentQuality = entertainer.testSkill(model, Skill.Entertain, Integer.MAX_VALUE, 0).getModifiedRoll();
        if (entertainer.getSP() > 0) {
            entertainer.addToSP(-1);
            println(entertainer.getName() + " exhausts 1 Stamina Point while entertaining the party.");
        } else {
            println(entertainer.getName() + " is tired (0 SP) and makes a sub-par performance.");
            entertainmentQuality -= 5;
        }

        for (GameCharacter gc : otherPartyMembers) {
            int roll = MyRandom.rollD10();
            int diff = entertainmentQuality - roll;
            if (diff > 0) {
                println(gc.getName() + " is entertained by the performance.");
                otherPartyMemberSay(gc, MyRandom.sample(
                        List.of("Spectacular!", "Wonderful!", "Please, do more!",
                                "Encore!", "I love it!", "You are great!", "This was some high quality entertainment.",
                                "I like what I see.", "Good stuff!", "Satisfying.")));
                attitudeMap.put(gc, attitudeMap.get(gc) + diff);
            } else {
                println(gc.getName() + " is not entertained by the performance.");
                otherPartyMemberSay(gc, MyRandom.sample(
                        List.of("I've seen better.", "Mediocre.", "Please stop.",
                                "Meh.", "Can't say I love this", "Really?", "This was some low quality entertainment.",
                                "Not my cup of tea.", "Less than good.", "Not so great.")));
                attitudeMap.put(gc, attitudeMap.get(gc) + diff/2);
            }
        }
    }

    private void offerCookedMeal(Model model) {
        print("Who should prepare the special meal for everybody?");
        GameCharacter cooker = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        model.getParty().addToFood(-otherPartyMembers.size());
        int cookQuality = cooker.testSkill(model, Skill.Survival, Integer.MAX_VALUE, 0).getModifiedRoll() + 3;
        if (cooker.getSP() > 0) {
            cooker.addToSP(-1);
            println(cooker.getFirstName() + " exhausts 1 Stamina Point while cooking the special meal.");
        } else {
            println(cooker.getName() + " is tired (0 SP) and makes a some mistakes while cooking the special meal.");
            cookQuality -= 5;
        }
        for (GameCharacter gc : otherPartyMembers) {
            int roll = MyRandom.rollD10();
            int diff = cookQuality - roll;
            if (diff > 0) {
                println(gc.getFirstName() + " enjoyed the special meal.");
                otherPartyMemberSay(gc,
                        MyRandom.sample(List.of("Yummy!", "Can I have some more?", "Ah, what lovely seasoning!<3",
                                "Perfection!3", "A splendid meal.", "So creamy!", "This is my favorite food.")));
                attitudeMap.put(gc, attitudeMap.get(gc) + diff);
            } else {
                println(gc.getFirstName() + " did not enjoy the special meal.");
                otherPartyMemberSay(gc, MyRandom.sample(
                        List.of("Yuck!", "Uhm, I think I've had enough.", "Is this a hair?",
                                "This is still raw.", "Definitely overcooked.", "Not my favorite.")));
                attitudeMap.put(gc, attitudeMap.get(gc) + diff/2);
            }
        }
    }

    private void offerGoldOrRations(Model model, boolean goldOffered) {
        print("How much do you want to offer? ");
        int amount;
        while (true) {
            try {
                amount = Integer.parseInt(lineInput());
                if (amount < 0) {
                    println("Please enter a positive number.");
                } else if (goldOffered && amount > model.getParty().getGold()) {
                    println("You cannot afford that.");
                } else if (!goldOffered && amount > model.getParty().getFood()) {
                    println("You don't have that much food.");
                } else {
                    break;
                }
            } catch (NumberFormatException nfe) {
                println("Please enter an integer.");
            }
        }
        int attitudeChange = 0;
        if (goldOffered) {
            if (amount < otherPartyLevel * 2) {
                otherLeaderSay("Is this some kind of bribe? I'm disgusted.");
                attitudeChange = -5;
            } else {
                otherLeaderSay("I welcome this fine gift.");
                model.getParty().addToGold(-amount);
                attitudeChange = Math.min(20, amount / 2);
            }
        } else {
            model.getParty().addToFood(-amount);
            otherLeaderSay("Ah, thank you. We were running a bit low on rations.");
            attitudeChange = Math.min(15, amount / 7);
        }
        attitudeMap.put(leader, attitudeMap.get(leader) + attitudeChange);
    }

    private void doAttack(Model model) {
        List<GameCharacter> unwilling = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        for (GameCharacter gc : otherPartyMembers) {
            giveRandomEquipment(model, gc);
            if (gc != leader) {
                if (attitudeTowardLeaderMap.get(gc) < -1) {
                    unwilling.add(gc);
                }
            }
            if (!unwilling.contains(gc)) {
                enemies.add(new FormerPartyMemberEnemy(gc));
            }
        }

        if (unwilling.size() == otherPartyMembers.size()-1 && !unwilling.isEmpty()) {
            otherPartyMemberSay(unwilling.get(0), "No way " + leader.getFirstName() + ", not this time. " +
                    "We're not following you into another idiotic fight.");
            if (unwilling.size() > 1) {
                otherPartyMemberSay(unwilling.get(1), "We're disbanding.");
            }
            otherLeaderSay("Oh so that's how it is? Fine, see how well you miscreants can do without me!");
            println(leader.getFirstName() + " stomps off in a rage.");
            otherPartyMembers.remove(leader);
            subView.removeFromParty(leader);
            otherPartyMemberSay(unwilling.get(0), "Good riddance. But really, what do we do now?");
            leaderSay("Wanna join our party instead?");

            unwilling.removeIf((GameCharacter gc) -> attitudeMap.get(gc) < 0);
            if (unwilling.size() > 0) {
                otherPartyMemberSay(unwilling.get(0), "Why not. You guys seem okay.");
                RecruitState state = new RecruitState(model, unwilling);
                state.run(model);
            } else {
                otherPartyMemberSay(otherPartyMembers.get(0), "No, I think I'll find my own way.");
            }
            return;
        } else if (unwilling.size() > 0) {
            otherPartyMemberSay(unwilling.get(0), "Nope. Not gonna fight these people, " + leader.getFirstName() +
                    ", I'm leaving.");
            for (int i = 1 ; i < unwilling.size(); ++i) {
                otherPartyMemberSay(unwilling.get(i),
                        MyRandom.sample(List.of("Me too.", "Same here.", "Count me out too.",
                                "Yeah, this is wrong.")));
            }
        }
        runCombat(enemies);
        if (model.getParty().isWipedOut()) {
            return;
        }
        CollapsingTransition.transition(model, subView);
        if (unwilling.size() > 0) {
            otherPartyMemberSay(unwilling.get(0), "Good riddance. But what do we do now?");
            leaderSay("Wanna join our party?");
            unwilling.removeIf((GameCharacter gc) -> attitudeMap.get(gc) < 0);
            if (unwilling.size() > 0) {
                otherPartyMemberSay(unwilling.get(0), MyRandom.sample(List.of("Why not. You guys seem okay.",
                        "Sure, why not.", "I don't have anything better to do.")));
                RecruitState state = new RecruitState(model, unwilling);
                state.run(model);
            } else {
                otherPartyMemberSay(otherPartyMembers.get(0), "No, I think I'll find my own way.");
            }
        }
    }

    private OtherPartySubView makeOtherPartySubView(Model model) {
        this.otherPartyMembers = new ArrayList<>();
        this.otherPartyLevel = MyRandom.randInt(2, 5);
        int baseAttitude = MyRandom.randInt(-35, Math.min(20, model.getParty().getReputation()*5));
        System.out.println("Base attitude of other party is: " + baseAttitude);
        int maxLeadership = -100;
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
            rando.setLevel(MyRandom.randInt(otherPartyLevel-1, otherPartyLevel+1));
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
            otherLeaderSay("We have a few things that we can bare to part with. " +
                            "For the right price of course.");
            List<Item> otherInventory = new ArrayList<>(model.getItemDeck().draw(MyRandom.randInt(6, 12)));
            int[] prices = new int[otherInventory.size()];
            int i = 0;
            for (Item it : otherInventory) {
                prices[i++] = Math.max(1, (int)Math.ceil(it.getCost() * (1.0 - (attitudeMap.get(leader) / 100.0))));
            }
            this.shop = new ShopState(model, "OTHER PARTY", otherInventory, prices);
        }
        this.shop.run(model);
    }

    private boolean talkToLeader(Model model) {
        int chosen = multipleOptionArrowMenu(model, 24, 33,
                List.of("Ask about " + himOrHer(leader.getGender()) + "self",
                        "Ask about recent success",
                        "Propose to merge parties"
                ));
        if (chosen == 0) {
            leaderSay("I'm " + model.getParty().getLeader().getFullName() + ". This is my team of adventurers. Who are you?");
            subView.revealInfo(leader);
            otherLeaderSay("I'm " + leader.getFullName() + ", " + leader.getCharClass().getFullName() + ". " +
                            "I'm the leader of this company.");
        } else if (chosen == 1) {
            leaderSay("How have you been fairing? Any luck with jobs, or quests or the like?");
            String reply;
            if (attitudeMap.get(leader) < -15) {
                reply = "I don't want to talk about that.";
            } else  if (baseAttitudeVsLeader == -2) {
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
            otherLeaderSay(reply);
        } else {
            leaderSay("How about we merge our parties into one?");
            otherLeaderSay("You're proposing a link-up between our companies?");
            if (otherPartyMembers.size() + model.getParty().size() > 8) {
                otherLeaderSay("I'm afraid the group would be to large to manage properly.");
            } else {
                int averageLevelDiff = (int)Math.round(Math.abs(calculateAverageLevel(model) - otherPartyLevel));
                int baseDifficulty = 12 - (int)Math.round(attitudeMap.get(leader) / 5.0) + averageLevelDiff * 2;
                SkillCheckResult result = model.getParty().getLeader().testSkill(model, Skill.Persuade, baseDifficulty);
                println(model.getParty().getLeader().getName() + " attempts to persuade " + leader.getName() + " to merge " +
                        "the parties (Persuade " + result.asString() + ").");
                if (result.isSuccessful()) {
                    otherLeaderSay("Alright. I accept.");
                    leaderSay("Great!");
                    for (GameCharacter gc : otherPartyMembers) {
                        giveRandomEquipment(model, gc);
                        model.getParty().add(gc);
                        println(gc.getFullName() + " joined the party.");
                    }
                    int gold = MyRandom.randInt(20, 50);
                    println(leader.getName() + " brought " + gold + " to the party.");
                    model.getParty().addToGold(gold);
                    int food = MyRandom.randInt(10, 40);
                    println(leader.getName() + " brought " + food + " to the party.");
                    model.getParty().addToFood(food);
                    int horsesAdded = 0;
                    for (int i = MyRandom.randInt(0, otherPartyMembers.size()); i > 0; ++i) {
                        if (model.getParty().canBuyMoreHorses()) {
                            model.getParty().getHorseHandler().addHorse(HorseHandler.generateHorse());
                            horsesAdded++;
                        }
                    }
                    println("The party has gained " + horsesAdded + " horses.");
                    return true;
                } else {
                    otherLeaderSay("No way. That just wouldn't be a good idea.");
                    attitudeMap.put(leader, attitudeMap.get(leader) - 5);
                }
            }
        }
        return false;
    }

    private void giveRandomEquipment(Model model, GameCharacter gc) {
        gc.getEquipment().setWeapon(model.getItemDeck().getRandomWeapon());
        while (gc.getEquipment().getClothing().isHeavy() && !gc.getCharClass().canUseHeavyArmor()) {
            gc.getEquipment().setClothing(model.getItemDeck().getRandomApparel());
        }
    }

    private void talkTo(Model model, GameCharacter who) {
        println("You approach " + who.getFullName() + ".");
        int chosen = multipleOptionArrowMenu(model, 24, 33,
                List.of("Ask about " + himOrHer(who.getGender()) + "self",
                        "Ask about " + hisOrHer(who.getGender()) + " party",
                        "Ask if " + (heOrShe(who.getGender())) + " wants to join"
                ));
        if (chosen == 0) {
            leaderSay(MyRandom.sample(List.of("Who are you?", "Tell me about yourself.")));
            subView.revealInfo(who);
            otherPartyMemberSay(who, "I'm " + who.getFullName() + ", " + who.getCharClass().getFullName() + ".");
            attitudeMap.put(leader, attitudeMap.get(leader) - 1);
        } else if (chosen == 1) {
            leaderSay(MyRandom.sample(List.of("How are things in your party?",
                    "What are your feelings about your party?",
                    "Has your party had much success in adventuring?")));
            String reply;
            if (attitudeMap.get(who) < -10) {
                reply = MyRandom.sample(List.of("No comment.", "I don't trust you enough to talk about that.",
                        "I don't want to talk about that."));
            } else {
                reply = getReply(attitudeTowardLeaderMap.get(who));
            }
            otherPartyMemberSay(who, reply);
            attitudeMap.put(leader, attitudeMap.get(leader) - 1);
        } else {
            leaderSay(MyRandom.sample(List.of("Are you interested in a job change?",
                    "Wanna come and work for me instead?", "Do you want to join my party instead?")));
            int baseDifficulty = 11 + attitudeTowardLeaderMap.get(who) * 3 -
                    ((int)Math.round(attitudeMap.get(who) / 5.0));
            SkillCheckResult result = model.getParty().getLeader().testSkill(model, Skill.Persuade, baseDifficulty);
            println(model.getParty().getLeader().getName() + " attempts to persuade " + who.getName() + " to join the " +
                    "party (Persuade " + result.asString() + ").");
            if (result.isSuccessful()) {
                attitudeMap.put(leader, attitudeMap.get(leader) - 20);
                RecruitState state = new RecruitState(model, List.of(who));
                state.run(model);
                model.setSubView(subView);
                if (model.getParty().getPartyMembers().contains(who)) {
                    otherPartyMembers.remove(who);
                    subView.removeFromParty(who);
                }
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

    private void otherPartyMemberSay(GameCharacter gc, String s) {
        getModel().getLog().waitForAnimationToFinish();
        String result = subView.addCallout(gc, s);
        printQuote(gc.getName(),result);
    }

    private void otherLeaderSay(String s) {
        otherPartyMemberSay(leader, s);
    }
}
