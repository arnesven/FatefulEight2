package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;
import model.states.EveningState;

import java.util.List;

public class ChasmEvent extends DailyEventState {
    public ChasmEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A deep chasm lies in front of the party. There is a very " +
                "narrow path and it will be difficult to traverse. Does the " +
                "party dare?");
        model.getParty().randomPartyMemberSay(model, List.of("This looks very difficult.",
                "I think we should turn back.", "Are we really going to do this?",
                "We've come so far. Going back will take so long..."));
        print("If you double back now, the journey will take you an extra day. Do you try to cross the chasm? (Y/N) ");
        if (yesNoInput()) {
            List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Acrobatics, 3);
            if (failers.isEmpty()) {
                println("The party manages to cross without incident.");
            } else {
                for (GameCharacter gc : failers) {
                    RiverEvent.characterDies(model, this, gc, "has fallen to " + hisOrHer(gc.getGender()) + " death!");
                    model.getLog().waitForAnimationToFinish();
                }
            }
        } else {
            println("You turn away from the chasm, but it is already late in the day.");
            new EveningState(model).run(model);
            println("You travel away away from the chasm and finally find a new route to take.");
        }
    }
}
