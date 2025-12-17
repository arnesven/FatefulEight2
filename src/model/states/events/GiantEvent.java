package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.DailyEventState;

public class GiantEvent extends DailyEventState {
    public GiantEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a strange large rock formation";
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Giant", "What seemed to be a rocky outcropping was actually the " +
                "closed fist of a stone giant. The humongous creature " +
                "suddenly moves and the earth shakes. There is no " +
                "fighting such a beast.");
        leaderSay("Everybody, get away!");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, gc, Skill.Acrobatics, 6, 10, 0);
            if (!result.isSuccessful()) {
                println(gc.getName() + " takes 1 damage.");
                gc.addToHP(-1);
            }
        }
        model.getLog().waitForAnimationToFinish();
        setFledCombat(true);
    }
}
