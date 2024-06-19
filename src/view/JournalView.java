package view;

import model.Model;
import model.QuestDeck;
import model.Summon;
import model.items.special.StoryItem;
import model.journal.*;
import model.map.UrbanLocation;
import model.states.events.RareBirdEvent;
import model.states.events.VisitMonasteryEvent;
import model.tasks.DestinationTask;
import model.travellers.Traveller;
import util.MyStrings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class JournalView extends TwoPaneSelectableListMenu {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 35;
    private static final int RIGHT_PANE_WIDTH = WIDTH/2;
    private ArrayList<JournalEntry> questsAndTasks;
    private FullMapView nextView = null;

    public JournalView(GameView previous) {
        super(previous, WIDTH, HEIGHT, RIGHT_PANE_WIDTH);
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        nextView = null;
        this.questsAndTasks = new ArrayList<>();
        addSummons(model);
        questsAndTasks.addAll(model.getMainStory().getMainStoryTasks(model));
        addMonasteryTask(model);
        addRareBirdTask(model);
        addGenericQuests(model);
        addTravellers(model);
        addDestinationTasks(model);
        sortQuestsAndTasks();
    }

    private void addDestinationTasks(Model model) {
        for (DestinationTask dt : model.getParty().getDestinationTasks()) {
            if (!dt.isCompleted() && !dt.isFailed(model)) {
                questsAndTasks.add(dt.getJournalEntry(model));
            } else {
                JournalEntry je = dt.getFailedJournalEntry(model);
                if (je != null) {
                    questsAndTasks.add(je);
                }
            }
        }
    }


    private void addGenericQuests(Model model) {
        for (QuestDeck.LocationAndQuest locationAndQuest : model.getQuestDeck().getLocationsAndQuests()) {
            QuestEntry entry = new QuestEntry(model, locationAndQuest.getLocation(),
                    locationAndQuest.getQuest(), locationAndQuest.getDay());
            if (entry.isValid()) {
                questsAndTasks.add(entry);
            }
        }
    }

    private void addTravellers(Model model) {
        for (Traveller t : model.getParty().getActiveTravellers()) {
            questsAndTasks.add(t.getJournalEntry(model, true, false));
        }
        for (Traveller t : model.getParty().getCompletedTravellers()) {
            questsAndTasks.add(t.getJournalEntry(model, false, true));
        }
        for (Traveller t : model.getParty().getAbandonedTravellers()) {
            questsAndTasks.add(t.getJournalEntry(model, false, false));
        }
    }

    private void addSummons(Model model) {
        for (Map.Entry<String, Summon> entry : model.getParty().getSummons().entrySet()) {
            UrbanLocation urb = model.getWorld().getUrbanLocationByPlaceName(entry.getKey());
            questsAndTasks.add(new SummonEntry(model, urb, entry.getValue()));
        }
    }

    private void sortQuestsAndTasks() {
        Collections.sort(questsAndTasks, (j1, j2) -> {
            if (j1.isComplete() == j2.isComplete()) {
                return j1.getName().compareTo(j2.getName());
            }
            int i1 = j1.isComplete() ? 1 : 0;
            int i2 = j2.isComplete() ? 1 : 0;
            return i1 - i2;
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
            if (je.getPosition(model) != null) {
                BorderFrame.drawString(model.getScreenHandler(), "Press F3 to see in map.",
                        x+1, y + parts.length + 5, textColor, MyColors.BLUE);
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
        if (questsAndTasks.isEmpty()) {
            return MyColors.WHITE;
        }
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

    @Override
    public void specificHandleEvent(KeyEvent keyEvent, Model model, int index) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F3) {
            if (!questsAndTasks.isEmpty()) {
                Point position = questsAndTasks.get(index).getPosition(model);
                this.nextView = new FullMapView(this, position);
                setTimeToTransition(true);
            }
        }
    }

    @Override
    public GameView getNextView(Model model) {
        if (nextView != null) {
            return nextView;
        }
        return super.getNextView(model);
    }


    private void addMonasteryTask(Model model) {
        if (VisitMonasteryEvent.hasVisited(model)) {
            questsAndTasks.add(new DonateAtMonasteryTask(model));
        }
    }


    private void addRareBirdTask(Model model) {
        if (RareBirdEvent.hasStarted(model)) {
            questsAndTasks.add(RareBirdEvent.makeJournalEntry(model));
        }
    }
}
