package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;
import model.states.EveningState;
import util.MyLists;
import util.MyPair;

import java.util.List;

public class SteepWallEvent extends DailyEventState {
    private static final int SOLO_DIFFICULTY = 7;
    private static final int COLLECTIVE_DIFFICULTY = 5;
    private DailyEventState innerEvent = null;

    public SteepWallEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The pass the party thought they were approaching turns out to be a dead end.");
        GameCharacter other = null;
        if (model.getParty().size() > 1) {
            other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "Hmm... " + model.getParty().getLeader().getFirstName() +
                    " did you read the map wrong?");
        }
        println("The only discernible way to get forward is up a steep mountain face.");
        leaderSay("Look's like there's no way to go but up.");
        if (other != null) {
            partyMemberSay(other, "Up that wall? That's going to be quite the effort.");
        }
        println("You could turn around and go back the way you came. But doing so would make you " +
                "lose the progress you've made today, you will have to stay in this hex tomorrow.");
        print("Alternatively, you could try to climb the mountain.");
        if (model.getParty().hasHorses()) {
            print(" Your horses will not be able to accompany you.");
        }
        print(" Do you climb the mountain (Y) or turn back (N)? ");
        if (yesNoInput()) {
            model.getParty().getHorseHandler().abandonHorses(model);
            climbMountain(model);
        } else {
            leaderSay("It's not worth the effort. Let's turn around.");
            stayInHex(model);
        }
    }

    private void stayInHex(Model model) {
        new EveningState(model, false, false, false).run(model);
        if (model.getParty().isWipedOut()) {
            return;
        }
        setCurrentTerrainSubview(model);
        innerEvent = model.getCurrentHex().generateEvent(model);
        innerEvent.run(model);
    }

    private void climbMountain(Model model) {

        boolean result;
        if (model.getParty().size() == 1) {
            result = model.getParty().doSoloSkillCheck(model, this, Skill.Acrobatics, SOLO_DIFFICULTY);
        } else {
            leaderSay("Get ready team, we're climbing.");
            print("Do you want to let one party member climb up with a rope first (Y) or do you let all party members " +
                    "climb together (N), helping each other as they go? ");
            if (yesNoInput()) {
                leaderSay("I think one of us should go up first with a rope. With a rope, it will be easier for the rest of us to climb.");
                MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Acrobatics, SOLO_DIFFICULTY);
                result = pair.first;
                if (result) {
                    println(pair.second.getFirstName() + " skillfully climbs up the mountain while the rest of the party remains below.");
                    partyMemberSay(pair.second, "Okay, I'm up. Here comes the rope.");
                    model.getLog().waitForAnimationToFinish();
                    model.getParty().benchPartyMembers(List.of(pair.second));
                    result = model.getParty().doCollectiveSkillCheck(model, this, Skill.Acrobatics, COLLECTIVE_DIFFICULTY - 2);
                    model.getParty().unbenchAll();
                }
            } else {
                leaderSay("I think we should all climb together. That way we can help each other along the way.");
                result = model.getParty().doCollectiveSkillCheck(model, this, Skill.Acrobatics, COLLECTIVE_DIFFICULTY);
            }
        }

        if (result) {
            println("The party has climbed up the mountain and enjoys a spectacular view! Each party member gains 10 additional experience points.");
            MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> model.getParty().giveXP(model, gc, 10));
        } else {
            println("The party has failed to climb the mountain and have now lost time on their journey.");
            stayInHex(model);
        }
    }


    @Override
    public boolean haveFledCombat() {
        if (innerEvent != null) {
            return innerEvent.haveFledCombat();
        }
        return super.haveFledCombat();
    }
}
