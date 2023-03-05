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

}
