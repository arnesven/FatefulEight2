package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.GameOverState;

import java.util.List;
import java.util.Locale;

public class RopeBridgeEvent extends RiverEvent {
    private boolean walkAway = false;

    public RopeBridgeEvent(Model model) {
        super(model);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return walkAway;
    }

    @Override
    protected void doEvent(Model model) {
        print("A bridge of rope and planks is hoisted over the river, " +
                "it looks very old and worn. Crossing will obviously be " +
                "perilous.");
        print(" Do you try? (Y/N) ");
        if (yesNoInput()) {
            List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Acrobatics, 4);
            if (failers.isEmpty()) {
                println("The party manages to cross without incident.");
            } else {
                for (GameCharacter gc : failers) {
                    SkillCheckResult result = gc.testSkill(Skill.Endurance, 8);
                    println(gc.getName() + " has fallen into the river and tries to swim across! " + result.asString());
                    if (result.isSuccessful()) {
                        model.getParty().partyMemberSay(model, gc, List.of("I'm okay!", "Gaah, that was tough!#", "Brrr, it was cold!",
                                "I just felt like having a dip."));
                    } else {
                        boolean wasLeader = gc.isLeader();
                        println(gc.getName() + " has been swept away by the current and drowns!");
                        model.getParty().remove(gc, false, false, 0);
                        if (model.getParty().size() == 0) {
                            println("Your last party member has been eliminated. Press any key to continue.");
                            waitForReturn();
                            model.setGameOver(true);
                            return;
                        } else {
                            model.getParty().randomPartyMemberSay(model, List.of(gc.getFirstName().toUpperCase() + "!!!"));
                            model.getParty().randomPartyMemberSay(model, List.of("..."));
                            model.getParty().randomPartyMemberSay(model, List.of("Gone! " + gc.getFirstName() + " is gone!"));
                            if (wasLeader) {
                                println(model.getParty().getLeader().getName() + " is now the new leader of the party.");
                            }
                        }
                    }
                }
            }
        } else {
            walkAway = true;
        }
    }
}
