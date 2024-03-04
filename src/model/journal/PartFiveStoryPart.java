package model.journal;

import model.Model;
import model.map.CastleLocation;
import model.map.WorldBuilder;
import model.map.locations.AncientStrongholdLocation;
import model.quests.AncientStrongholdQuest;
import model.quests.OrcWarCampQuest;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.awt.*;
import java.util.List;

public class PartFiveStoryPart extends StoryPart {
    private static final int QUEST_DONE = 1;
    private final String castleName;
    private int internalStep = 0;

    public PartFiveStoryPart(String castleName) {
        this.castleName = castleName;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new GoToAncientStrongholdEntry());
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress() {
        internalStep++;
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return null;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (model.getCurrentHex().getLocation() != null &&
                model.getCurrentHex().getLocation() instanceof AncientStrongholdLocation && internalStep < QUEST_DONE) {
            CastleLocation loc = model.getWorld().getCastleByName(castleName);
            quests.add(getQuestAndSetPortrait(AncientStrongholdQuest.QUEST_NAME, model.getLordPortrait(loc), loc.getLordName()));
        }
    }

    @Override
    protected boolean isCompleted() {
        return false;
    }

    private class GoToAncientStrongholdEntry extends MainStoryTask {
        public GoToAncientStrongholdEntry() {
            super("The Ancient Stronghold");
        }

        @Override
        public String getText() {
            if (internalStep == 0) {
                return "Travel to the location of the Ancient Stronghold.";
            }
            return "Return to " + castleName + " to report your success.";
        }

        @Override
        public boolean isComplete() {
            return PartFiveStoryPart.this.isCompleted();
        }

        @Override
        public Point getPosition(Model model) {
            if (internalStep == 0) {
                return WorldBuilder.getFortressPosition(model.getMainStory().getExpandDirection());
            }
            return model.getMainStory().getCastlePosition(model);
        }
    }
}
