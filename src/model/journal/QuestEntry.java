package model.journal;

import model.Model;
import model.map.WorldType;
import model.quests.OfferedQuest;
import java.awt.*;

public class QuestEntry implements JournalEntry {
    private final String questText;
    private final OfferedQuest offeredQuest;

    public QuestEntry(OfferedQuest offeredQuest, String questText) {
        this.offeredQuest = offeredQuest;
        this.questText = questText;
    }

    @Override
    public boolean isTask() {
        return false;
    }

    @Override
    public Point getPosition(Model model) {
        if (offeredQuest.isAccepted()) {
            return offeredQuest.getRemotePosition();
        }
        return offeredQuest.getOfferPosition();
    }

    @Override
    public WorldType getWorld() {
        return offeredQuest.getWorld();
    }

    @Override
    public String getName() {
        return offeredQuest.getQuestName();
    }

    @Override
    public String getText() {
        StringBuilder bldr = new StringBuilder();
        String locationName = offeredQuest.getLocationName();
        if (offeredQuest.isAccepted()) {
            if (locationName != null) {
                bldr.append("You accepted this quest in ").append(locationName).append(" on day ").append(offeredQuest.getAcceptedOnDay()).append(".\n\n");
            } else {
                bldr.append("You accepted this quest on day ").append(offeredQuest.getAcceptedOnDay()).append(".\n\n");
            }
            bldr.append(questText);
            bldr.append("\n\n");
            if (isComplete()) {
                bldr.append("You successfully completed this quest!");
            } else {
                bldr.append("This quest is in progress.");
            }
        } else {
            bldr.append("You have been offered this quest by ").append(offeredQuest.getProvider()).append(" in ");
            if (locationName != null) {
                bldr.append(locationName).append(".");
            } else {
                Point position = offeredQuest.getOfferPosition();
                bldr.append("hex (").append(position.x).append(", ").append(position.y).append(").");
            }
        }

        return bldr.toString();
    }

    @Override
    public boolean isComplete() {
        return offeredQuest.isCompleted();
    }

    @Override
    public boolean isFailed() {
        return false;
    }
}
