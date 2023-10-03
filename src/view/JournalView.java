package view;

import model.Model;
import model.QuestDeck;
import model.Summon;
import model.journal.*;
import model.map.UrbanLocation;
import util.MyStrings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class JournalView extends TwoPaneSelectableListMenu {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 35;
    private static final int RIGHT_PANE_WIDTH = WIDTH/2;
    private ArrayList<JournalEntry> questsAndTasks;

    public JournalView(GameView previous) {
        super(previous, WIDTH, HEIGHT, RIGHT_PANE_WIDTH);
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        this.questsAndTasks = new ArrayList<>();
        for (Map.Entry<String, Summon> entry : model.getParty().getSummons().entrySet()) {
            UrbanLocation urb = model.getWorld().getUrbanLocationByPlaceName(entry.getKey());
            questsAndTasks.add(new SummonEntry(model, urb, entry.getValue()));
        }
        for (JournalEntry task : model.getMainStory().getMainStoryTasks(model)) {
            questsAndTasks.add(task);
        }
        for (QuestDeck.LocationAndQuest locationAndQuest : model.getQuestDeck().getLocationsAndQuests()) {
            questsAndTasks.add(new QuestEntry(model, locationAndQuest.getLocation(),
                    locationAndQuest.getQuest(), locationAndQuest.getDay()));
        }
        Collections.sort(questsAndTasks, new Comparator<JournalEntry>() {
            @Override
            public int compare(JournalEntry j1, JournalEntry j2) {
                if (j1.isComplete() == j2.isComplete()) {
                    return j1.getName().compareTo(j2.getName());
                }
                int i1 = j1.isComplete() ? 1 : 0;
                int i2 = j2.isComplete() ? 1 : 0;
                return i1 - i2;
            }
        });
    }

    @Override
    protected void drawContent(Model model, int index, int x, int y) {
        if (questsAndTasks.isEmpty()) {
            print(model.getScreenHandler(), x+1, y+2, "You have no entries");
            print(model.getScreenHandler(), x+1, y+3, "In your journal yet.");
        } else {
            JournalEntry je = questsAndTasks.get(index);
            String[] parts = MyStrings.partitionWithLineBreaks(je.getText(), RIGHT_PANE_WIDTH);
            BorderFrame.drawString(model.getScreenHandler(), je.isTask()?"Task":"Quest", x+1, y + 1,
                    MyColors.WHITE, MyColors.BLUE);
            MyColors textColor = MyColors.WHITE;
            for (int row = 0; row < parts.length; ++row) {
                if (parts[row].toLowerCase().contains("completed")) {
                    textColor = MyColors.YELLOW;
                }
                BorderFrame.drawString(model.getScreenHandler(), parts[row], x+1, y + row + 3,
                        textColor, MyColors.BLUE);
            }
        }
    }

    @Override
    protected String getHeading() {
        return "Quests and Tasks";
    }

    @Override
    protected String getEntryName(int index) {
        if (questsAndTasks.size() == 0) {
            return " ";
        }

        String completeString = "";
        if (questsAndTasks.get(index).isComplete()) {
            completeString = "" + (char)0xB6;
        } else if (questsAndTasks.get(index).isFailed()) {
            completeString = "" + (char)0xB7;
        }

        return questsAndTasks.get(index).getName() + completeString;
    }

    @Override
    protected MyColors getEntryColor(int index) {
        if (questsAndTasks.get(index).isComplete()) {
            return MyColors.GRAY;
        } else if (questsAndTasks.get(index).isFailed()) {
            return MyColors.RED;
        }
        return super.getEntryColor(index);
    }

    @Override
    protected int getNumberOfEntries() {
        if (questsAndTasks.size() == 0) {
            return 1;
        }
        return questsAndTasks.size();
    }

}
