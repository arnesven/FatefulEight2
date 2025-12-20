package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.VampirismCondition;
import model.states.DailyEventState;
import model.states.feeding.VampireFeedingHouse;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class FeedOnPartyMemberEvent extends DailyEventState {
    private final GameCharacter vampire;

    public FeedOnPartyMemberEvent(Model model, GameCharacter vampire) {
        super(model);
        this.vampire = vampire;
    }

    @Override
    protected void doEvent(Model model) {
        MyLists.forEach(model.getParty().getPartyMembers(), ch -> model.getParty().forceEyesClosed(ch, true));
        model.getParty().forceEyesClosed(vampire, false);
        GameCharacter victim;
        do {
            print("Who do you want " + vampire.getFirstName() + " to feed on?");
            victim =  model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            if (victim != model.getParty().getLeader() &&
                    !CheckForVampireEvent.isVampire(victim)) {
                break;
            } else {
                println("Unfortunately a vampire cannot feed on itself or another vampire.");
            }
        } while (true);
        List<GameCharacter> sleepers = new ArrayList<>(model.getParty().getPartyMembers());
        sleepers.remove(vampire);
        sleepers.remove(victim);
        model.getParty().benchPartyMembers(sleepers);
        MyLists.forEach(sleepers, ch -> model.getParty().forceEyesClosed(ch, false));

        println(vampire.getFirstName() + " waits until the rest of the party has gone to bed. " +
                "Then, carefully he approaches " + victim.getFirstName() + "'s bed.");
        if (!remainsUndetected(model, vampire, victim, sleepers)) {
            return;
        }

        VampireProwlNightEvent vampireProwl = new VampireProwlNightEvent(model, true);
        println(vampire.getFirstName() + " leans over " + victim.getFirstName() + "...");
        VampirismCondition vampCond = (VampirismCondition) vampire.getCondition(VampirismCondition.class);
        if (vampireProwl.failsToDetectVampire(model, this, victim)) {
            model.getParty().enabledVampireLookFor(vampire);
            vampireProwl.makeVampire(model, victim, vampCond.getStage());
            if (victim.getAppearance() instanceof AdvancedAppearance) {
                VampireFeedingHouse.applyRaceSpecificEffect(model, this, vampire,
                        (AdvancedAppearance) victim.getAppearance());
            } else {
                VampireFeedingHouse.applyRaceSpecificEffect(model, this, vampire,
                        (AdvancedAppearance) vampire.getAppearance());
            }
            model.getLog().waitForAnimationToFinish();
            model.getParty().disableVampireLookFor(vampire);
        } else {
            println(victim.getFirstName() + " wakes up!");
            model.getLog().waitForAnimationToFinish();
            model.getParty().forceEyesClosed(victim, false);
            CheckForVampireEvent vampireEvent = new CheckForVampireEvent(model);
            if (!vampireEvent.askForMesmerize(model, vampire, victim, vampCond)) {
                partyMemberSay(victim, "Help! A vampire!!! What, " + vampire.getFirstName() + ", you're a vampire?");
                model.getLog().waitForAnimationToFinish();
                if (model.getParty().size() > 2) {
                    println("The rest of the party members quickly wake up and grab their weapons.");
                    model.getParty().unbenchAll();
                }
                vampireEvent.dealWithVampire(model, vampire, victim);
            }
        }
        model.getParty().unbenchAll();
        MyLists.forEach(model.getParty().getPartyMembers(),
                ch -> model.getParty().forceEyesClosed(ch, false));
    }

    private boolean remainsUndetected(Model model, GameCharacter vampire, GameCharacter victim, List<GameCharacter> sleepers) {
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, vampire,
                Skill.Sneak, 3 + model.getParty().size(), 10, 0);
        if (!result.isSuccessful()) {
            model.getParty().forceEyesClosed(victim, false);
            GameCharacter other = victim;
            if (model.getParty().size() > 2) {
                other = MyRandom.sample(sleepers);
                println(victim.getFirstName() + "'s dark deed has been detected! (Sneak " + result.asString() + ")");
                model.getParty().unbenchAll();
                partyMemberSay(other, "What's going on here. " + vampire.getFirstName() +
                        " what are you doing to " + victim.getFirstName() + "?");
                partyMemberSay(victim, "I think... I think " + heOrShe(vampire.getGender()) + " was trying to bite me! " +
                        "Wait a minute, " + heOrShe(vampire.getGender()) + "'s a bloody vampire!");
            } else {
                partyMemberSay(victim, "I think... I think you were trying to bite me! " +
                        "Wait a minute, you're a bloody vampire!");
                if (new CheckForVampireEvent(model).askForMesmerize(model, vampire, victim,
                        (VampirismCondition) vampire.getCondition(VampirismCondition.class))) {
                    return false;
                }
            }
            new CheckForVampireEvent(model).dealWithVampire(model, vampire, other);
            return false;
        }
        println(vampire.getFirstName() + " remains undetected. (Sneak " + result.asString() + ")");
        return true;
    }
}
