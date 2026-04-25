package model.quests;

import model.Model;
import model.states.DailyEventState;

public class DefaultQuestIntro extends DailyEventState {
    private final Quest quest;

    public DefaultQuestIntro(Model model, Quest quest) {
        super(model);
        this.quest = quest;
    }

    @Override
    protected void doEvent(Model model) {
        println("You have been offered a quest: " + quest.getName());
    }
}
