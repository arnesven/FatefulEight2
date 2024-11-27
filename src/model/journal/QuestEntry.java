package model.journal;

import model.mainstory.MainStory;
import model.Model;
import model.QuestDeck;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.quests.MainQuest;
import model.quests.Quest;

import java.awt.*;

public class QuestEntry implements JournalEntry {
    private boolean invalid = false;
    private boolean completed = false;
    private boolean success = false;
    private final int day;
    private Quest quest = null;
    private HexLocation hexLocation;

    public QuestEntry(Model model, String location, String quest, int day) {
        this.day = day;
        for (Quest q : MainStory.getQuests()) {
            if (q.getName().equals(quest)) {
                this.quest = q;
            }
        }
        if (this.quest == null) {
            for (Quest q : QuestDeck.getAllQuests()) {
                if (q.getName().equals(quest)) {
                    this.quest = q;
                    break;
                }
            }

            hexLocation = findLocation(model, location);
            if (hexLocation != null) {
                this.completed = model.getQuestDeck().wasSuccess(model, this.quest) ||
                        model.getQuestDeck().wasFailure(model, this.quest);
                if (completed) {
                    this.success = model.getQuestDeck().wasSuccess(model, this.quest);
                }
            }
        } else { // Main Quest
            this.completed = ((MainQuest)this.quest).isCompleted(model);
            this.success = true;
        }

        if (this.quest == null) {
            this.invalid = true;
            System.err.println("Quest still null! Could not find matching '" + quest + "'");
        }
    }

    private static HexLocation findLocation(Model model, String locationName) {
        HexLocation location = null;
        for (UrbanLocation urb : model.getWorld().getLordLocations()) {
            if (((HexLocation) urb).getName().equals(locationName)) {
                location = (HexLocation) urb;
                break;
            }
        }
        return location;
    }

    @Override
    public boolean isTask() {
        return false;
    }

    @Override
    public Point getPosition(Model model) {
        if (hexLocation != null) {
            return model.getWorld().getPositionForLocation(hexLocation);
        }
        return null;
    }

    @Override
    public String getName() {
        return quest.getName();
    }

    @Override
    public String getText() {
        StringBuilder bldr = new StringBuilder();
        if (hexLocation != null) {
            bldr.append("You accepted this quest in " + hexLocation.getName() + " on day " + day + ".\n\n");
        } else {
            bldr.append("You accepted this quest on day " + day + ".\n\n");
        }
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

    public boolean isValid() {
        return !invalid;
    }
}
