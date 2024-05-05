package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.BanditArcherEnemy;
import model.enemies.BanditEnemy;
import model.enemies.BanditLeaderEnemy;
import model.map.UrbanLocation;
import util.MyLists;
import util.MyPair;
import util.MyUnaryFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BurySomeGoldEvent extends PersonalityTraitEvent {

    public BurySomeGoldEvent(Model model, PersonalityTrait personalityTrait, GameCharacter mainCharacter) {
        super(model, personalityTrait, mainCharacter);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getParty().getGold() >= 200 &&
                !(model.getCurrentHex().getLocation() instanceof UrbanLocation);
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = getMainCharacter();
        println(main.getFullName() + " approaches you.");
        partyMemberSay(main, "Hey, how much money do we have?");
        leaderSay("A fair amount. Why do you want to know?");
        partyMemberSay(main, "Is it really wise to carry around so much gold? " +
                "The weight of our money bags is making me nervous! " +
                "What if we're ambushed by robbers?");
        leaderSay("There's always a risk of that I suppose.");
        partyMemberSay(main, "Yeah, so I was thinking... why don't we bury some of it?");
        leaderSay("What, like pirates?");
        partyMemberSay(main, "Exactly! Isn't it brilliant? We bury the gold. The robbers ambush us and we " +
                "can honestly say that we don't have any gold. Later, when they're gone, we just dig it up!");
        leaderSay("Hmmm... what if we forget where we've buried it?");
        partyMemberSay(main, "We could make a map.");
        leaderSay("What if somebody steals the map?");
        partyMemberSay(main, "Hey... I get the feeling you don't like my idea here...");
        print("Bury some of your gold? (Y/N) ");

        int buriedAmount = 0;
        if (yesNoInput()) {
            leaderSay("Okay " + main.getFirstName() + ", you make some good points. Let's bury some of our gold.");
            println("How much gold do you want to bury?");
            List<Integer> amounts = new ArrayList<>(List.of(50,
                    model.getParty().getGold()/2,
                    model.getParty().getGold()-20,
                    model.getParty().getGold()));
            Collections.sort(amounts);
            List<String> options = MyLists.transform(amounts, integer -> integer.toString() + " Gold");
            int choice = multipleOptionArrowMenu(model, 24, 24, options);
            buriedAmount = amounts.get(choice);
            model.getParty().addToGold(-buriedAmount);
            println("The party digs a fairly deep hole at the roots of a nearby tree. " +
                    buriedAmount + " gold is placed in a bag. The hole is then filled up.");
            leaderSay("There. A long term deposit! Feel better " + main.getFirstName() + "?");
            partyMemberSay(main, "Yes. Much better. And there's no way we'll forget this very memorable tree.");
            leaderSay("I hope so. I'm still think you are being a bit paranoid though. " +
                    "It's not like there are robbers behind every bush...");
        } else {
            leaderSay("I'm sorry " + main.getFirstName() + ", I just don't think it's a good idea.");
            partyMemberSay(main, "Okay, but you'll be sorry when the robbers come!");
            leaderSay("I'll take my chances. It's not like there are robbers behind every bush...");
        }
        model.getLog().waitForAnimationToFinish();
        showRandomPortrait(model, Classes.THF, "Robber");
        println("A group of six cloaked rogues step out from behind a hedge.");
        portraitSay("Good afternoon!");
        leaderSay("You got to be kidding me.");
        portraitSay("Stand and deliver!");
        partyMemberSay(main, "What did I tell you?");

        MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 12);
        if (result.first) {
            partyMemberSay(result.second,
                    "Unfortunately we just spent our last coins on a coffin for our fallen party member. " +
                    "We buried him next to the tree here. Poor sod. He caught the pox.");
            portraitSay("THE POX? And you lot have been touching him?");
            partyMemberSay(result.second, "Yes... you're right... We're done for! Will you help us?");
            portraitSay("Don't come near me you sickness ridden filth!");
            println("The robbers ride away with haste.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            GameCharacter other = model.getParty().getLeader();
            if (model.getParty().getLeader() == result.second) {
                other = main;
            }
            partyMemberSay(other, "Nice bluff " + result.second.getFirstName() + ". Perhaps it was a bit risky though. " +
                    "What if he had insisted we'd dig it up?");
        } else {
            partyMemberSay(result.second, "We have absolutely no money at all. Don't even think about looking in our bags!");
            portraitSay("You know, something tells me you're not being completely honest. I think I'd " +
                    "like to have a look in your bags.");
            print("Let the robbers search your belongings? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Sure... nothing but some moldy rations in there anyway.");
                if (model.getParty().getGold() < 20) {
                    portraitSay("Hmmm... you were right, not much of value here. That's odd, I could swear you were lying. " +
                            "Well, no use hanging about here. Farewell you poor sods.");
                    model.getLog().waitForAnimationToFinish();
                    removePortraitSubView(model);
                } else {
                    portraitSay("Hmmm... yes... but what's this, a coin purse?");
                    leaderSay("Drat.");
                    print("Attack the robbers? (Y/N) ");
                    if (yesNoInput()) {
                        leaderSay("Okay, that's it. You're dead!");
                        portraitSay("Come on then maggot!");
                        doRobberCombat(model, buriedAmount);
                    } else {
                        leaderSay("Fine, take it.");
                        int amountLost = (model.getParty().getGold() / 20) * 20;
                        println("The party hands over " + amountLost + " gold to the robbers.");
                        model.getParty().addToGold(-amountLost);
                        portraitSay("We'll be on our way now. Thanks for everything chappies.");
                        model.getLog().waitForAnimationToFinish();
                        removePortraitSubView(model);
                        if (buriedAmount == 0) {
                            partyMemberSay(main, model.getParty().getLeader().getFirstName() + ", I don't want to say " +
                                    "'I told you so', but...");
                            leaderSay("Go ahead.");
                            partyMemberSay(main, "I told you so.");
                        }
                    }
                }
            } else {
                leaderSay("Over my dead body scum.");
                portraitSay("That can be arranged fool.");
                doRobberCombat(model, buriedAmount);
            }
        }

        if (haveFledCombat()) {
            if (buriedAmount > 0) {
                leaderSay("We're never gonna find that tree...");
            }
            return;
        }

        if (buriedAmount > 0) {
            leaderSay("Okay. Now let's find the tree.");
            partyMemberSay(main, "Wait, you're not going to dig up the gold are you?");
            leaderSay("Of course, the robbers are gone.");
            partyMemberSay(main, "THOSE robbers are gone. What about other robbers?");
            leaderSay("Now you're just being silly.");
            partyMemberSay(main, "Well, it's your call. You know what happened last time...");
            leaderSay(main.getFirstName() + ", which one of these trees was it?");
            if (model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 8)) {
                partyMemberSay(main, "It's right here. It must be.");
                println("You dig a hole and find the gold exactly where you left it.");
                model.getParty().addToGold(buriedAmount);
                println("The party gains " + buriedAmount + " gold.");
            } else {
                partyMemberSay(main, "Uhm... this one. I think... Or was it this one?");
                leaderSay("Oh-uh.");
                partyMemberSay(main, "I'm sure if we look a bit more we'll find the tree.");
                println("The party spends the rest of the day trying to find the right tree. Unfortunately, the " +
                        "commotion with the robbers must have trampled the ground and there is no longer any trace " +
                        "of the buried gold. With heads hung low, you leave this place.");
            }
        }
    }

    private void doRobberCombat(Model model, int buriedAmount) {
        runCombat(List.of(new BanditEnemy('B'), new BanditArcherEnemy('A'), new BanditArcherEnemy('A'),
                new BanditArcherEnemy('A'), new BanditEnemy('B'), new BanditLeaderEnemy('C')));
        possiblyGetHorsesAfterCombat("Robbers", 6);
        setCurrentTerrainSubview(model);
    }
}
