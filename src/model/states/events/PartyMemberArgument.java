package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class PartyMemberArgument extends DailyEventState {
    public PartyMemberArgument(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() < 3) {
            new NoEventState(model).doEvent(model);
            return;
        }
        List<GameCharacter> chars = new ArrayList<>(model.getParty().getPartyMembers());
        chars.remove(model.getParty().getLeader());
        GameCharacter charaA = MyRandom.sample(chars);
        chars.remove(charaA);
        GameCharacter charaB = MyRandom.sample(chars);
        model.getParty().partyMemberSay(model, charaA,
                List.of("... You can't be serious!#", "That just isn't true!#",
                "Get off my back!#", charaB.getFirstName() + ", you are such a ...#",
                        "You are really getting on my nerves " + charaB.getFirstName() + "!#"));
        println(charaA.getFirstName() + " and " + charaB.getFirstName() + " are having an argument.");
        List<String> options = new ArrayList<>(List.of(
                "Take " + charaA.getFirstName() + "'s side",
                "Take " + charaB.getFirstName() + "'s side",
                "Promote reconciliation",
                "Stay out of it"));

        int result = multipleOptionArrowMenu(model, 24, 15, options);
        if (result == 0) {
            takeSide(model, charaA, charaB);
        } else if (result == 1) {
            takeSide(model, charaB, charaA);
        } else if (result == 2) {
            leaderSay("Look guys, let's calm down and talk about this.");
            SkillCheckResult checkResult = model.getParty().doSkillCheckWithReRoll(model, this,
                    model.getParty().getLeader(), Skill.Persuade, 12, 0,
                    model.getParty().getLeader().getRankForSkill(Skill.Leadership));
            if (checkResult.isSuccessful()) {
                println("You manage to calm the party members down and resolve the argument.");
                model.getParty().partyMemberSay(model, charaB,
                        List.of("Fair enough, I guess I didn't think about it that way.", "I was wrong.",
                                "I'm sorry " + charaA.getFirstName() + ".", "Okay, I'll back off."));
                charaB.addToAttitude(model.getParty().getLeader(), 2);
                charaA.addToAttitude(model.getParty().getLeader(), 2);
                charaA.addToAttitude(charaB, 2);
                charaB.addToAttitude(charaA, 2);
            } else {
                println("Your interference in the argument seem to have only made things worse, and now " +
                        "they are angry at you too.");
                charaB.addToAttitude(model.getParty().getLeader(), -5);
                charaA.addToAttitude(model.getParty().getLeader(), -5);
                stormOff(model, charaA, charaB);
            }
        } else {
            stormOff(model, charaB, charaA);
        }

    }

    private void stormOff(Model model, GameCharacter charaA, GameCharacter charaB) {
        println("The argument escalates quickly into a shouting match. After a while " + charaA.getFirstName() +
                " storms off, but returns after a little while. Both " + heOrShe(charaA.getGender()) + " and " +
                charaB.getFirstName() + " remain moody and silent for the rest of the day.");
        charaA.addToAttitude(charaB, -10);
        charaB.addToAttitude(charaA, -10);
    }

    private void takeSide(Model model, GameCharacter charaA, GameCharacter charaB) {
        leaderSay(charaA.getFirstName() + "'s is right. Back off " + charaB.getFirstName() + ".");
        partyMemberSay(charaB, "Hrmph!");
        charaB.addToAttitude(model.getParty().getLeader(), -10);
        charaA.addToAttitude(model.getParty().getLeader(), 10);
    }
}
