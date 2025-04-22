package model.mainstory.honorable;

import model.Model;
import model.mainstory.GainSupportOfHonorableWarriorsTask;
import model.states.DailyEventState;

public class MeetLordShingenEvent extends DailyEventState {
    private final GainSupportOfHonorableWarriorsTask task;

    public MeetLordShingenEvent(Model model, GainSupportOfHonorableWarriorsTask gainSupportOfHonorableWarriorsTask) {
        super(model);
        this.task = gainSupportOfHonorableWarriorsTask;
    }

    @Override
    protected void doEvent(Model model) {
        println("You finally meet with Lord Shingen");
        showExplicitPortrait(model, task.getShingenPortrait(), "Lord Shingen");
        portraitSay("Ah yes... The westerner I've heard so much about. I've heard old Miko has had you running errands for us.");
        model.getLog().waitForReturn();
    }
}
