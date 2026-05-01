package model.quests;

import model.Model;
import model.TimeOfDay;
import model.states.DailyEventState;

public abstract class QuestIntroEventState extends DailyEventState {
    public QuestIntroEventState(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Time passes.");
        model.setTimeOfDay(TimeOfDay.EVENING);
        setCurrentTerrainSubview(model);
        runQuestIntro(model);
    }

    protected abstract void runQuestIntro(Model model);
}
