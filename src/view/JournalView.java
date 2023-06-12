package view;

import model.Model;
import model.QuestDeck;
import model.Summon;
import model.map.CastleLocation;
import model.map.HexLocation;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.quests.Quest;
import model.tasks.SummonTask;
import util.MyStrings;

import java.util.ArrayList;
import java.util.Locale;
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
        for (QuestDeck.LocationAndQuest locationAndQuest : model.getQuestDeck().getLocationsAndQuests()) {
            questsAndTasks.add(new QuestEntry(model, locationAndQuest.getLocation(),
                    locationAndQuest.getQuest(), locationAndQuest.getDay()));
        }

    }

    @Override
    protected void drawContent(Model model, int index, int x, int y) {
        if (questsAndTasks.isEmpty()) {
            print(model.getScreenHandler(), x+1, y+2, "You have no entries");
            print(model.getScreenHandler(), x+1, y+3, "In your journal yet.");
        } else {
            JournalEntry je = questsAndTasks.get(index);
            String[] parts = MyStrings.partitionWithLineBreaks(je.getText(), RIGHT_PANE_WIDTH);
            MyColors textColor = MyColors.WHITE;
            for (int row = 0; row < parts.length; ++row) {
                if (parts[row].contains("completed")) {
                    textColor = MyColors.YELLOW;
                }
                BorderFrame.drawString(model.getScreenHandler(), parts[row], x+1, y + row + 1,
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
    protected int getNumberOfEntries() {
        if (questsAndTasks.size() == 0) {
            return 1;
        }
        return questsAndTasks.size();
    }

    private interface JournalEntry {
        String getName();
        String getText();
        boolean isComplete();
        boolean isFailed();
    }

    private static class SummonEntry implements JournalEntry {
        private final UrbanLocation urb;
        private final Summon summon;
        private final boolean complete;
        private SummonTask task;

        public SummonEntry(Model model, UrbanLocation urb, Summon summon) {
            this.urb = urb;
            this.summon = summon;
            if (summon.getStep() > Summon.ACCEPTED) {
                this.task = summon.getTask(model, urb);
            }
            this.complete = summon.getStep() == Summon.COMPLETE;
        }

        @Override
        public String getName() {
            String title = urb.getLordTitle().substring(0, 1).toUpperCase() +
                    urb.getLordTitle().substring(1);
            if (urb instanceof TownLocation) {
                return title + " of " + ((TownLocation) urb).getTownName();
            }
            return title + " of " + urb.getPlaceName();
        }

        @Override
        public String getText() {
            StringBuilder bldr = new StringBuilder();
            bldr.append("Task\n\n");
            if (summon.getStep() == Summon.ACCEPTED) {
                bldr.append("The " + urb.getLordTitle() + " of " + urb.getPlaceName() +
                            " has requested your presence.\n\n");
                bldr.append((urb.getLordGender()?"She":"He") + " asks that you visit " +
                        (urb.getLordGender()?"her":"him") + " at the ");
                if (urb instanceof TownLocation) {
                    bldr.append("town hall of ");
                }
                bldr.append(urb.getPlaceName());
                bldr.append(".\n\n");
                bldr.append("You have no additional information.");
            } else {
                bldr.append("We have met with the " + urb.getLordTitle() + " " + urb.getLordName() + " of " + urb.getPlaceName() + "\n\n");
                bldr.append((urb.getLordGender() ? "She" : "He") + " wants us to help " +
                        (urb.getLordGender() ? "her" : "him") + " with a special problem;\n\n");
                bldr.append(task.getJournalDescription());

                if (summon.getStep() == Summon.COMPLETE) {
                    bldr.append("\n\nYou completed this task on day " + summon.getCompletedOnDay() + ".");
                }
            }

            return bldr.toString();
        }

        @Override
        public boolean isComplete() {
            return complete;
        }

        @Override
        public boolean isFailed() {
            return false;
        }
    }

    private static class QuestEntry implements JournalEntry {
        private boolean completed = false;
        private boolean success = false;
        private final int day;
        private Quest quest;
        private HexLocation hexLocation;

        public QuestEntry(Model model, String location, String quest, int day) {
            this.day = day;
            for (UrbanLocation urb : model.getWorld().getLordLocations()) {
                if (((HexLocation)urb).getName().equals(location)) {
                    this.hexLocation = (HexLocation)urb;
                }
            }
            for (Quest q : QuestDeck.getAllQuests()) {
                if (q.getName().equals(quest)) {
                    this.quest = q;
                }
            }
            this.completed = model.getQuestDeck().hasFlagIn(hexLocation);
            if (completed) {
                this.success = model.getQuestDeck().wasSuccessfulIn(hexLocation);
            }
        }

        @Override
        public String getName() {
            return quest.getName();
        }

        @Override
        public String getText() {
            StringBuilder bldr = new StringBuilder();
            bldr.append("Quest\n\n");
            bldr.append("You accepted this quest in " + hexLocation.getName() + " on day " + day + ".\n\n");
            bldr.append(quest.getText());
            bldr.append("\n\n");
            if (completed) {
                if (success) {
                    bldr.append("You successfully completed this quest!");
                } else {
                    bldr.append("You failed this quest.");
                }
            } else {
                bldr.append("This quest is in progress.");
            }
            return bldr.toString();
        }

        @Override
        public boolean isComplete() {
            return completed && success;
        }

        @Override
        public boolean isFailed() {
            return completed && !success;
        }
    }
}
