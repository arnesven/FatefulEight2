package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class PartyCookingEvent extends DailyEventState {
    public PartyCookingEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        List<GameCharacter> cooks = new ArrayList<>();
        for (GameCharacter chara : model.getParty().getPartyMembers()) {
            if (chara.testSkillHidden(Skill.Survival, 10, 0).isSuccessful()) {
                cooks.add(chara);
            }
        }
        int rationsNeeded = model.getParty().size() / 2;
        if (cooks.isEmpty() || model.getParty().size() < 2 || rationsNeeded > model.getParty().getFood()) {
            new NoEventState(model).doEvent(model);
            return;
        }
        GameCharacter cooker = MyRandom.sample(cooks);
        println(cooker.getFirstName() + " has offered to prepare a special meal for the party, but it will require effort and extra rations.");
        print("Do you permit the special meal to be prepared? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("We can't waste rations on elaborate recipes, we need to make them last.");
            return;
        }

        int cookQuality = cooker.testSkill(model, Skill.Survival).getModifiedRoll() + 3;
        if (cooker.getSP() > 0) {
            cooker.addToSP(-1);
            println(cooker.getFirstName() + " exhausts 1 Stamina Point while cooking the special meal.");
        } else {
            println(cooker.getName() + " is tired (0 SP) and makes a some mistakes while cooking the special meal.");
            cookQuality -= 5;
        }

        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != cooker) {
                int roll = MyRandom.rollD10();
                int diff = cookQuality - roll;
                if (diff > 0) {
                    println(gc.getFirstName() + " enjoyed the special meal.");
                    if (gc.hasPersonality(PersonalityTrait.gluttonous)) {
                        partyMemberSay(gc, "GIVE... ME... MORE!");
                    } else {
                        model.getParty().partyMemberSay(model, gc,
                                List.of("Yummy!", "Can I have some more?", "Ah, what lovely seasoning!<3",
                                        "Perfection!<3", "So creamy!", "This is my favorite food."));
                    }
                    gc.addToAttitude(cooker, diff);
                    cooker.addToAttitude(gc, diff/2);
                } else {
                    println(gc.getFirstName() + " did not enjoy the special meal.");
                    if (gc.hasPersonality(PersonalityTrait.unkind) ||
                            gc.hasPersonality(PersonalityTrait.rude) ||
                            gc.hasPersonality(PersonalityTrait.cold)) {
                        model.getParty().partyMemberSay(model, gc,
                                List.of("Yuck!", "You are an awful cook", "Bloody terrible!",
                                        "Did you follow a recipe, or did you just toss random things into the pot?",
                                        "Are you trying to poison me?"));
                    } else {
                        model.getParty().partyMemberSay(model, gc,
                                List.of("Uhm, I think I've had enough.", "Is this a hair?",
                                        "This is still raw.", "Definitely overcooked.", "Not my favorite."));
                    }
                    gc.addToAttitude(cooker, diff/2);
                    cooker.addToAttitude(gc, diff/4);
                }
            }
        }
        model.getLog().waitForAnimationToFinish();
        showPartyAttitudesSubView(model);
    }
}
