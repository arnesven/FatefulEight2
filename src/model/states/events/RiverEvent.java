package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.DailyEventState;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.List;

public abstract class RiverEvent extends DailyEventState {

    public static SubView subView = new ImageSubView("river", "RIVER", "You are attempting to cross the river.", true);

    public RiverEvent(Model model) {
        super(model);
    }

    public abstract boolean eventPreventsCrossing(Model model);

    public void fallIntoRiver(Model model, GameCharacter gc, String text) {
        SkillCheckResult result;
        do {
            result = gc.testSkill(Skill.Endurance, 8);
            println(gc.getName() + " " + text + " " + result.asString() + ".");
            if (gc.getSP() > 0) {
                println("Using stamina to re-roll.");
                gc.addToSP(-1);
            } else {
                break;
            }
        } while (!result.isSuccessful());

        if (result.isSuccessful()) {
            model.getParty().partyMemberSay(model, gc, List.of("I'm okay!", "Gaah, that was tough!#", "Brrr, it was cold!",
                    "I just felt like having a dip."));
        } else {
            characterDies(model, this, gc," has been swept away by the current and drowns!");
        }
    }

    public static void characterDies(Model model, DailyEventState event, GameCharacter gc, String reason) {
        boolean wasLeader = gc.isLeader();
        event.println(gc.getName() + reason);
        model.getParty().remove(gc, false, false, 0);
        if (model.getParty().size() == 0) {
            event.println("Your last party member has been eliminated. Press any key to continue.");
            event.waitForReturn();
            model.setGameOver(true);
        } else {
            model.getParty().randomPartyMemberSay(model, List.of(gc.getFirstName().toUpperCase() + "!!!"));
            model.getParty().randomPartyMemberSay(model, List.of("..."));
            model.getParty().randomPartyMemberSay(model, List.of("Gone! " + gc.getFirstName() + " is gone!"));
            if (wasLeader) {
                event.println(model.getParty().getLeader().getName() + " is now the new leader of the party.");
            }
        }
    }

}
