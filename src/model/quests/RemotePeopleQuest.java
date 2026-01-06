package model.quests;

import model.Model;
import model.journal.PartSixStoryPart;
import model.journal.StoryPart;
import model.mainstory.GainSupportOfRemotePeopleTask;
import model.states.GameState;
import model.states.QuestState;

public abstract class RemotePeopleQuest extends MainQuest {
    private GainSupportOfRemotePeopleTask task;

    public RemotePeopleQuest(String name, String provider, QuestDifficulty difficulty, Reward reward,
                             int moveAfter, String text, String endText) {
        super(name, provider, difficulty, reward, moveAfter, text, endText);
    }

    @Override
    public void setStoryPart(StoryPart storyPart) {
        super.setStoryPart(storyPart);
        this.task = ((PartSixStoryPart)storyPart).getRemotePeopleTask();
    }

    public GainSupportOfRemotePeopleTask getTask() {
        return task;
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        GameState toReturn = super.endOfQuest(model, state, questWasSuccess);
        if (questWasSuccess) {
            task.setQuestSuccessful(this);
        }
        return toReturn;
    }
}
