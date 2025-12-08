package model.mainstory;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.special.WillisCharacter;
import model.journal.*;
import model.map.*;
import model.quests.*;
import model.states.EveningState;
import model.states.InitialLeadsEveningState;
import model.states.dailyaction.TownDailyActionState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.MyColors;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainStory implements Serializable {

    public static final Map<String, MainQuest> QUESTS = makeMainStoryQuests();

    private boolean townVisited = false;
    private boolean castleVisited = false;
    private boolean templeVisited = false;
    private final List<StoryPart> storyParts = new ArrayList<>();
    private MainStorySpawnLocation spawnData;
    private final GameCharacter caidCharacter = new CaidCharacter();
    private final GameCharacter willisCharacter = new WillisCharacter();
    private boolean caidQuestDone;


    public MainStory() {
        List<MainStorySpawnLocation> spawnDataList = List.of(
                new MainStorySpawnEast(),
                //new MainStorySpawnSouth(), // TODO: Add when finished
                new MainStorySpawnNorth(),
                new MainStorySpawnWest()
        );
        spawnData = MyRandom.sample(spawnDataList);
    }

    public void progressStoryForTesting(Model model, MainStoryStep part, MainStorySpawnLocation spawnData) {
        if (spawnData != null) {
            this.spawnData = spawnData;
        }
        System.out.println("Progressing main story to step " + part.ordinal());
        for (MainStoryStep p : MainStoryStep.values()) {
            if (part.ordinal() >= p.ordinal()) {
                System.out.print("  Step " + p.ordinal());
                p.progress(model, this);
                System.out.println(" done (" + p.toString() + ")");
            }
        }
    }

    public EveningState generateInitialLeadsEveningState(Model model, boolean freeLodging, boolean freeRations) {
        if (isStarted() || model.getCurrentHex().getLocation() instanceof UrbanLocation || model.getParty().size() < 2) {
            return null;
        }
        return new InitialLeadsEveningState(model, freeLodging, freeRations);
    }

    public List<JournalEntry> getMainStoryTasks(Model model) {
        if (isStarted()) {
            List<JournalEntry> result = new ArrayList<>();
            for (StoryPart sp : storyParts) {
                result.addAll(sp.getJournalEntries());
            }
            result.add(new VisitTask("Town", townVisited));
            result.add(new VisitTask("Castle", castleVisited));
            result.add(new VisitTask("Temple", templeVisited));
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

    public void setupStory(GameCharacter whosUncle) {
        storyParts.add(new InitialStoryPart(whosUncle, spawnData.getTown()));
        spawnData.setAncientStrongholdCode(AncientStrongholdModel.generateCode());
    }

    public TownLocation getStartingLocation(Model model) {
        return model.getWorld().getTownByName(spawnData.getTown());
    }

    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        townVisited = true;
        if (isStarted()) {
            for (StoryPart sp : storyParts) {
                sp.handleTownSetup(townDailyActionState);
            }
        }
    }

    public boolean isStarted() {
        return !storyParts.isEmpty();
    }

    public void addQuests(Model model, List<Quest> quests) {
        if (isStarted()) {
            for (StoryPart sp : storyParts) {
                sp.addQuests(model, quests);
            }
        }
        for (Quest q : quests) {
            q.setRemoteLocation(model);
            if (q instanceof MainQuest) {
                ((MainQuest)q).setSpawnData(spawnData);
            }
        }
    }

    public static List<Quest> getQuests() {
        return new ArrayList<>(QUESTS.values());
    }

    private static Map<String, MainQuest> makeMainStoryQuests() {
        Map<String, MainQuest> map = new HashMap<>();
        FrogmenProblemQuest frogmen = new FrogmenProblemQuest();
        map.put(frogmen.getName(), frogmen);
        RescueMissionQuest rescue = new RescueMissionQuest();
        map.put(rescue.getName(), rescue);
        SpecialDeliveryQuest delivery = new SpecialDeliveryQuest();
        map.put(delivery.getName(), delivery);
        TroubleInTheLibraryQuest libraryQuest = new TroubleInTheLibraryQuest();
        map.put(libraryQuest.getName(), libraryQuest);
        HelpWillisQuest helpWillis = new HelpWillisQuest();
        map.put(helpWillis.getName(), helpWillis);
        OrcWarCampQuest orcWarCamp = new OrcWarCampQuest();
        map.put(orcWarCamp.getName(), orcWarCamp);
        AncientStrongholdQuest strongholdQuest = new AncientStrongholdQuest();
        map.put(strongholdQuest.getName(), strongholdQuest);
        VampiresLairQuest vampireQuest = new VampiresLairQuest();
        map.put(vampireQuest.getName(), vampireQuest);
        EscapeTheDungeonQuest escapeQuest = new EscapeTheDungeonQuest();
        map.put(escapeQuest.getName(), escapeQuest);
        AvertTheMutinyQuest mutinyQuest = new AvertTheMutinyQuest();
        map.put(mutinyQuest.getName(), mutinyQuest);
        NightAtTheTheaterQuest nightAtTheater = new NightAtTheTheaterQuest();
        map.put(nightAtTheater.getName(), nightAtTheater);
        SavageVikingsQuest vikingsQuest = new SavageVikingsQuest();
        map.put(vikingsQuest.getName(), vikingsQuest);
        MindMachineQuest machineQuest = new MindMachineQuest();
        map.put(machineQuest.getName(), machineQuest);
        return map;
    }

    public static MainQuest getQuest(String key) {
        return QUESTS.get(key);
    }

    public VisitLordEvent getVisitLordEvent(Model model, UrbanLocation location) {
        for (StoryPart sp : storyParts) {
            VisitLordEvent event = sp.getVisitLordEvent(model, location);
            if (event != null) {
                return event;
            }
        }
        return null;
    }

    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        if (isStarted()) {
            for (StoryPart sp : storyParts) {
                sp.drawMapObjects(model, x, y, screenX, screenY);
            }
        }
    }

    public List<DailyAction> getDailyActionsForHex(Model model, WorldHex worldHex) {
        List<DailyAction> result = new ArrayList<>();
        for (StoryPart sp : storyParts) {
            result.addAll(sp.getDailyActions(model, worldHex));
        }
        return result;
    }

    public GameCharacter getCaidCharacter() {
        return caidCharacter;
    }

    public GameCharacter getWillisCharacter() {
        return willisCharacter;
    }

    public int getExpandDirection() {
        return spawnData.getExpandDirection();
    }

    public void addStoryPart(StoryPart nextPart) {
        storyParts.add(nextPart);
    }

    public String getCastleName() {
        return spawnData.getCastle();
    }

    public String getLibraryTownName() {
        return spawnData.getLibraryTown();
    }

    public Point getWitchPosition() {
        return spawnData.getWitch();
    }

    public List<StoryPart> getStoryParts() {
        return storyParts;
    }

    public String getExpandDirectionName() {
        if (spawnData.getExpandDirection() == WorldBuilder.EXPAND_EAST) {
            return "east";
        } else if (spawnData.getExpandDirection() == WorldBuilder.EXPAND_SOUTH) {
            return "south";
        } else if (spawnData.getExpandDirection() == WorldBuilder.EXPAND_WEST) {
            return "west";
        }
        return "north";
    }

    public Point getCampPosition() {
        return spawnData.getCamp();
    }

    public String getHexInfo(Point position) {
        StringBuilder bldr = new StringBuilder();
        for (StoryPart part : storyParts) {
            String info = part.getHexInfo(position);
            if (info != null) {
                bldr.append(", " + info);
            }
        }
        return bldr.toString();
    }

    public MyColors[] getAncientStrongholdCode() {
        return spawnData.getCode();
    }

    public Point getXelbiPosition() {
        return spawnData.getXelbi();
    }

    public boolean isCaidQuestDone() {
        return caidQuestDone;
    }

    public void setCaidQuestDone(boolean caidQuestDone) {
        this.caidQuestDone = caidQuestDone;
    }

    public Point getCastlePosition(Model model) {
        return model.getWorld().getPositionForLocation(model.getWorld().getCastleByName(getCastleName()));
    }

    public boolean isCompleted(Model model) {
        return false; // TODO: Update when done.
        //return getQuest(AncientStrongholdQuest.QUEST_NAME).isCompleted(model);
    }

    public boolean isFugitive() {
        return MyLists.any(storyParts, (StoryPart sp) -> sp instanceof PartSixStoryPart);
    }

    public boolean isPersonaNonGrata(Model model) {
        return isFugitive() && model.getWorld().getKingdomForPosition(model.getParty().getPosition()).getName().equals(spawnData.getCastle());
    }

    public GainSupportOfRemotePeopleTask makeRemotePeopleSupportTask(Model model) {
        return spawnData.makeRemotePeopleSupportTask(model);
    }

    public String getRemotePeopleName() {
        return spawnData.remotePeopleName();
    }

    public List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model) {
        return spawnData.makeNeighborKingdomTasks(model);
    }

    public List<MyPair<String, String>> getFactionStrings() {
        List<MyPair<String, String>> result = new ArrayList<>();
        if (isFugitive()) {
            result.add(new MyPair<>(CastleLocation.placeNameToKingdom(spawnData.getCastle()),
                    "Persona Non Grata"));
        }
        for (StoryPart sp : storyParts) {
            sp.addFactionStrings(result);
        }
        return result;
    }

    public World buildPastWorld() {
        return spawnData.buildPastWorld();
    }

    public Point getPastEntryPosition() {
        return spawnData.getPastEntryPoint();
    }
}
