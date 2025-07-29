package model.journal;

import model.Model;
import model.quests.MindMachineQuest;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.awt.*;
import java.util.List;

public class PartSevenStoryPart extends StoryPart {
    private final String castle;

    public PartSevenStoryPart(Model model, String castle) {
        this.castle = castle;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new JournalEntry() {
            @Override
            public String getName() {
                return "The Prophecy of the Quad";
            }

            @Override
            public String getText() {
                return "Complete the quest " + MindMachineQuest.QUEST_NAME + ".";
            }

            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                return null;
            }
        });
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress() {

    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return null;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {

    }

    @Override
    protected boolean isCompleted() {
        return false;
    }
}
