package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.CharacterAppearance;
import model.map.WorldHex;
import model.quests.EscapeTheDungeonQuest;
import model.quests.Quest;
import model.quests.SpecialDeliveryQuest;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;
import util.MyLists;

import java.awt.*;
import java.util.List;

public class PartSixStoryPart extends StoryPart {
    private final String castle;
    private int internalStep = 0;

    public PartSixStoryPart(String castleName) {
        this.castle = castleName;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new EscapeTheDungeonJournalEntry(castle));
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) { }

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

    }

    @Override
    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        Point witchPoint = model.getMainStory().getWitchPosition();
        if (witchPoint.x == x && witchPoint.y == y && internalStep > 0) {
            model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 1);
        }
    }

    @Override
    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) {
        Point witchPoint = model.getMainStory().getWitchPosition();
        Point hexPoint = model.getWorld().getPositionForHex(worldHex);
        if (witchPoint.x == hexPoint.x && witchPoint.y == hexPoint.y) {
            return List.of(new DailyAction("Visit Witch", new VisitWitchEvent(model)));
        }
        return super.getDailyActions(model, worldHex);
    }

    @Override
    protected boolean isCompleted() {
        return false;
    }

    private class EscapeTheDungeonJournalEntry extends MainStoryTask {
        public EscapeTheDungeonJournalEntry(String castle) {
            super("Escape " + castle);
        }

        @Override
        public String getText() {
            if (internalStep == 0) {
                return "Complete the '" + EscapeTheDungeonQuest.QUEST_NAME + "' quest.";
            }
            return "Seek refuge at the witch of the woods.";
        }

        @Override
        public boolean isComplete() {
            return PartSixStoryPart.this.isCompleted();
        }

        @Override
        public Point getPosition(Model model) {
            if (internalStep == 0) {
                return model.getMainStory().getCastlePosition(model);
            }
            return model.getMainStory().getWitchPosition();
        }
    }

    private static class VisitWitchEvent extends DailyEventState {
        private final CharacterAppearance witchAppearance;

        public VisitWitchEvent(Model model) {
            super(model);
            WitchStoryPart witchStoryPart = (WitchStoryPart) MyLists.find(model.getMainStory().getStoryParts(),
                    (StoryPart sp) -> sp instanceof WitchStoryPart);
            this.witchAppearance = witchStoryPart.getWitchAppearance();
        }

        @Override
        protected boolean isFreeRations() {
            return true;
        }

        @Override
        protected void doEvent(Model model) {
            showExplicitPortrait(model, witchAppearance, "Witch");
            portraitSay("Oh... you lot.");
        }
    }
}
