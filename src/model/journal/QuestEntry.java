package model.journal;

import model.Model;
import model.QuestDeck;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.quests.Quest;

public class QuestEntry implements JournalEntry {
    private boolean completed = false;
    private boolean success = false;
    private final int day;
    private Quest quest;
    private HexLocation hexLocation;

    public QuestEntry(Model model, String location, String quest, int day) {
        this.day = day;
        for (UrbanLocation urb : model.getWorld().getLordLocations()) {
            if (((HexLocation) urb).getName().equals(location)) {
                this.hexLocation = (HexLocation) urb;
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
    public boolean isTask() {
        return false;
    }

    @Override
    public String getName() {
        return quest.getName();
    }

    @Override
    public String getText() {
        StringBuilder bldr = new StringBuilder();
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
