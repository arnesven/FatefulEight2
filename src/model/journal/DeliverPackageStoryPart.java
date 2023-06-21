package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.classes.Classes;
import model.map.WorldHex;
import model.quests.Quest;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.dailyaction.TownDailyActionState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.sprites.SpriteQuestMarker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DeliverPackageStoryPart extends StoryPart {

    private static final Sprite MAP_SPRITE = new SpriteQuestMarker();
    private final Point witchPoint;
    private int internalStep = 0;

    public DeliverPackageStoryPart(Point witchPosition) {
        this.witchPoint = witchPosition;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        if (internalStep > 0) {
            return List.of(new FindTheWitch());
        }
        return new ArrayList<>();
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress(int track) {
        internalStep++;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {

    }

    @Override
    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        if (internalStep > 0) {
            if (witchPoint.x == x && witchPoint.y == y) {
                model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 2);
            }
        }
    }

    @Override
    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) {
        if (internalStep > 0) {
            Point hexPoint = model.getWorld().getPositionForHex(worldHex);
            if (witchPoint.x == hexPoint.x && witchPoint.y == hexPoint.y) {
                return List.of(new DailyAction("Find Witch", new VisitWitchEvent(model)));
            }
        }
        return super.getDailyActions(model, worldHex);
    }

    @Override
    public StoryPart transition(Model model) {
        return null;
    }

    public static class FindTheWitch extends MainStoryTask {
        public FindTheWitch() {
            super("Find the Witch");
        }

        @Override
        public String getText() {
            return "Find Everix's acquaintance, the witch, to ask about the crimson pearl.";
        }

        @Override
        public boolean isComplete() {
            return false;
        }
    }

    private class VisitWitchEvent extends DailyEventState {
        public VisitWitchEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            showRandomPortrait(model, Classes.WIT, Race.ALL, "Witch");
            portraitSay("Bubble bubble!");
            leaderSay("Hello?");
            // TODO: More
        }
    }
}
