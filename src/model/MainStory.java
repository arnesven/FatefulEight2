package model;

import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.journal.*;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.map.locations.AshtonshireTown;
import model.map.locations.EbonshireTown;
import model.map.locations.LowerThelnTown;
import model.map.locations.SouthMeadhomeTown;
import model.quests.FrogmenProblemQuest;
import model.quests.Quest;
import model.states.EveningState;
import model.states.InitialLeadsEveningState;
import model.states.dailyaction.TownDailyActionState;
import model.states.dailyaction.VisitUncleNode;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainStory implements Serializable {

    private static final FrogmenProblemQuest FROGMEN_QUEST = new FrogmenProblemQuest();
    private boolean initialLeadsEventGiven = false;
    private boolean townVisited = false;
    private boolean castleVisited = false;
    private boolean templeVisited = false;
    private String startLocation = null;
    private GameCharacter whosUncle;
    private int storyStep = 0; // replace with state machine
    private AdvancedAppearance unclePortrait;

    public EveningState generateInitialLeadsEveningState(Model model, boolean freeLodging, boolean freeRations) {
        if (initialLeadsEventGiven || model.getCurrentHex().getLocation() instanceof UrbanLocation || model.getParty().size() < 2) {
            return null;
        }
        return new InitialLeadsEveningState(model, freeLodging, freeRations);
    }

    public List<JournalEntry> getMainStoryTasks(Model model) {
        if (initialLeadsEventGiven) {
            List<JournalEntry> result = new ArrayList<>();
            if (startLocation != null) {
                result.add(new FirstMainStoryTask(startLocation, whosUncle, storyStep));
            }
            result.add(new VisitTask("Town", townVisited));
            result.add(new VisitTask("Castle", castleVisited));
            result.add(new VisitTask("Temple", templeVisited)); // TODO: Add some info on temples
            result.add(new RuinsEntry(model));
            return result;
        }
        return new ArrayList<>();
    }

    public void setVisitedTown(boolean b) {
        townVisited = b;
    }

    public void setVisitedCastle(boolean b) {
        castleVisited = b;
    }

    public void setVisitedTemple(boolean b) {
        templeVisited = b;
    }

    public void setInitialLeadsGiven(boolean b) {
        initialLeadsEventGiven = b;
    }

    public TownLocation getStartingLocation(Model model, GameCharacter whosUncle) {
        if (startLocation == null) {
            List<String> towns = List.of(
                            new AshtonshireTown().getName(),
                            new SouthMeadhomeTown().getName(),
                            new EbonshireTown().getName()
            );
            startLocation = MyRandom.sample(towns);
            this.whosUncle = whosUncle;
            this.unclePortrait = PortraitSubView.makeRandomPortrait(Classes.None, whosUncle.getRace());
        }
        return model.getWorld().getTownByName(startLocation);
    }

    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        townVisited = true;
        //if (storyStep == 0) {
            if (townDailyActionState.getTown().getName().equals(startLocation)) {
                int randomSeed = townDailyActionState.getTown().getName().hashCode();
                townDailyActionState.addNodeInFreeSlot(new VisitUncleNode(townDailyActionState.getTown(), whosUncle, unclePortrait), randomSeed);
            }
        //}
    }

    public void increaseStep() {
        this.storyStep++;
    }

    public void addQuests(Model model, List<Quest> quests) {
        if (storyStep == 1) {
            if (model.getCurrentHex().getLocation() != null && model.getCurrentHex().getLocation() instanceof TownLocation) {
                TownLocation loc = (TownLocation) model.getCurrentHex().getLocation();
                if (loc.getName().equals(startLocation)) {
                    FROGMEN_QUEST.setPortrait(unclePortrait);
                    quests.add(FROGMEN_QUEST);
                }
            }
        }
    }

    public static List<Quest> getQuests() {
        return List.of(FROGMEN_QUEST);
    }

    public int getStep() {
        return storyStep;
    }
}
