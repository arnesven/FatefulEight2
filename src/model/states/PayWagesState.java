package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PayWagesState extends GameState{
    private static final int[] WAGE_SIZES = new int[]{5, 10, 20};

    public PayWagesState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        GameCharacter leader = model.getParty().getLeader();
        if (model.getParty().size() > 1) {
            GameCharacter rando = model.getParty().getRandomPartyMember(leader);
            partyMemberSay(rando, "Finally a payday. " +
                    leader.getFirstName() + ", time to pay us our wages.");
            model.getTutorial().wages(model);
            int wages = selectWage(model);
            if (wages == 0) {
                leaderSay("Sorry, no wages this time. The company needs it for provisions and gear.");
                if (rando.hasPersonality(PersonalityTrait.forgiving)) {
                    partyMemberSay(rando, "I guess that's reasonable.");
                    leaderSay("Thank you for understanding");
                } else {
                    partyMemberSay(rando, "What!? This is an outrage!");
                    rando.addToAttitude(leader, -10);
                }
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    if (gc != leader && gc != rando) {
                        if (gc.hasPersonality(PersonalityTrait.forgiving)) {
                            partyMemberSay(gc, "I guess it's okay. I don't really need my wages right now.");
                        } else {
                            partyMemberSay(gc, MyRandom.sample(List.of("Despicable!", "Unbelievable!",
                                    "That sucks!", "How disappointing.", "That's low, " + leader.getFirstName(),
                                    "I should have joined another party.", "I want you to know that this upsets me.",
                                    "Grrr!")));
                            gc.addToAttitude(leader, -10);
                        }
                    }
                }
            } else {
                println(leader.getName() + " paid " + wages +
                        " gold to each other party member.");
                model.getParty().addToGold(-1 * wages * (model.getParty().size()-1));
                adjustAttitudes(model, wages/2);
                if (rando.hasPersonality(PersonalityTrait.greedy)) {
                    partyMemberSay(rando, "I expected more.");
                    leaderSay("Of course you did.");
                } else {
                    if (wages == WAGE_SIZES[0]) {
                        partyMemberSay(rando, "Paltry. But better than nothing.");
                    } else if (wages == WAGE_SIZES[1]) {
                        partyMemberSay(rando, "A decent pay, but I expected more.");
                    } else {
                        partyMemberSay(rando, "A generous pay out! Three cheers for " + leader.getFirstName() + "!");
                    }
                }
            }

        }
        return null;
    }

    private int selectWage(Model model) {
        List<String> options = new ArrayList<>(List.of("Pay no wages"));
        for (int wage : WAGE_SIZES) {
            if ((model.getParty().size() - 1) * wage <= model.getParty().getGold()) {
                options.add("Pay " + wage + " gold to each");
            }
        }
        int choice = multipleOptionArrowMenu(model, 24, 24, options);
        if (choice == 0) {
            return 0;
        }
        return WAGE_SIZES[choice-1];
    }

    private static void adjustAttitudes(Model model, int attitude) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != model.getParty().getLeader()) {
                gc.addToAttitude(model.getParty().getLeader(), attitude);
            }
        }
    }

    public void explicitPayWages(Model model) {
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
            leaderSay(MyRandom.sample(List.of("You guys doing alright?",
                    "Ready to head out?", "How's it going?", "Everything OK over here?", "Enjoying the establishment?")));
            super.showPartyAttitudesSubView(model);
            GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(gc, MyRandom.sample(List.of("Just having a brew...", "I'm loving it.3",
                    "Ready for action!", "Whenever you're ready boss.", "Ready to roll.")));

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

    private void  bribePartyMember(GameCharacter target, int bribe) {
        target.addToAttitude(getModel().getParty().getLeader(), goldToAttitudeBonus(bribe));
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

    private static int goldToAttitudeBonus(int bribe) {
        return bribe / 2;
    }
}
