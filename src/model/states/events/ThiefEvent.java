package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.FacialExpression;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.Lockpick;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class ThiefEvent extends DailyEventState {
    private final boolean withIntro;

    public ThiefEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    public ThiefEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        showSilhouettePortrait(model, "Stranger");
        if (withIntro) {
            showEventCard("A stranger comes up to you and asks for directions.");
        }
        if (spotThief(model)) {
            partyMemberSay(model.getParty().getRandomPartyMember(), "Did you think you could pull a fast one on us?", FacialExpression.angry);
            printQuote("Thief", "Come on guys, I just need enough for food, and maybe a beer.");
            print("Do you treat the thief to some rations and a drink? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Well, why didn't you say so. You're welcome to " +
                                "spend the evening with us, we've got plenty of food and drink.");
                randomSayIfPersonality(PersonalityTrait.generous, new ArrayList<>(),
                        "You're probably very hungry. Eat as much as you like.");
                model.getParty().addToFood(-1);
                ChangeClassEvent change = new ChangeClassEvent(model, Classes.THF);
                println("The thief is surprised by your sudden generosity and gladly joins you for the evening.");
                println("The thief offers to sell you some lockpicks.");
                new ShopState(model, "Thief", makeLockpicks(), new boolean[]{false});
                print("The thief is also willing to show you some tricks, ");
                change.areYouInterested(model);
            } else {
                partyMemberSay(model.getParty().getRandomPartyMember(), "Shove off you! Don't let us catch you near us again!",
                        FacialExpression.angry);
            }
        } else {
            println("Slightly confused over the stranger's line of questions you try your best to answer. " +
                    "Later you realize that one of your money purses has disappeared");
            model.getParty().randomPartyMemberSay(model, List.of("Dammit. I knew that guy was trouble."));
            int lost = 10;
            if (model.getParty().getGold() < 10) {
                lost = model.getParty().getGold();
            }
            println("The party loses " + lost + " gold.");
            model.getParty().loseGold(lost);
            boolean didSay = randomSayIfPersonality(PersonalityTrait.irritable, List.of(model.getParty().getLeader()),
                    "Darn it! we need to keep a better eye on things.");
            if (!didSay) {
                randomSayIfPersonality(PersonalityTrait.critical, List.of(model.getParty().getLeader()),
                        "Darn it! " + model.getParty().getLeader().getName() + ", you need to keep a better eye on things.");

            }
        }

    }

    private List<Item> makeLockpicks() {
        ArrayList<Item> list = new ArrayList<>();
        for (int i = MyRandom.rollD6(); i > 0; --i) {
            list.add(new Lockpick());
        }
        return list;
    }

    private boolean spotThief(Model model) {
        MyPair<SkillCheckResult, GameCharacter> resultAndSpotter = doPassiveSkillCheck(Skill.Perception, 8);
        if (resultAndSpotter.first.isSuccessful()) {
            GameCharacter gc = resultAndSpotter.second;
            println(gc.getName() + " spots the stranger trying to snatch your purse. (Perception " +
                    resultAndSpotter.first.asString() + ")");
            partyMemberSay(gc, "Hey, there! THIEF!", FacialExpression.angry);
            return true;
        }
        return false;
    }
}
