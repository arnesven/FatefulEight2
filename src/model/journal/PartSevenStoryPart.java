package model.journal;

import model.Model;
import model.quests.MindMachineQuest;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.awt.*;
import java.util.List;

public class PartSevenStoryPart extends StoryPart {
    private static final int INITIAL_STEP = 0;
    private static final int TRAVELED_TO_THE_PAST = 1;
    private final String castle;
    private int step = INITIAL_STEP;

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
                if (step == INITIAL_STEP) {
                    return "Complete the quest " + MindMachineQuest.QUEST_NAME + ".";
                }
                return "In an attempt to stop Arabella's plans you tried to destroy her Mind Machine. " +
                        "The machine malfunctioned and transported you to an unknown location.\n\n" +
                        "Now you need to figure out where you are.";
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
        step++;
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
