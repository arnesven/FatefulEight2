package model.journal;

import model.Model;
import model.map.CastleLocation;
import model.map.locations.AncientStrongholdLocation;
import model.quests.AncientStrongholdQuest;
import model.quests.OrcWarCampQuest;
import model.quests.Quest;
import model.states.dailyaction.TownDailyActionState;

import java.util.List;

public class PartFiveStoryPart extends StoryPart {
    private final String castleName;

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

    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return null;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (model.getCurrentHex().getLocation() != null &&
                model.getCurrentHex().getLocation() instanceof AncientStrongholdLocation) {
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
            return "Travel to the location of the Ancient Stronghold.";
        }

        @Override
        public boolean isComplete() {
            return PartFiveStoryPart.this.isCompleted();
        }
    }
}
