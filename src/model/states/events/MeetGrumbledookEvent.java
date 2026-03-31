package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;
import model.states.GameState;

public class MeetGrumbledookEvent extends DailyEventState {
    private final GameCharacter victim;
    private final FindGrumbledookTask task;

    public MeetGrumbledookEvent(Model model, GameCharacter victim, FindGrumbledookTask task) {
        super(model);
        this.victim = victim;
        this.task = task;
    }


    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        leaderSay("Let's see if we can find this enchanter fellow.");
        waitForReturn();
    }
}
