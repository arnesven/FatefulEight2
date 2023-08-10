package model;

import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.KruskTalandro;
import model.characters.WillisCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.journal.*;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.quests.*;
import model.races.Race;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.InitialLeadsEveningState;
import model.states.dailyaction.TownDailyActionState;
import util.MyRandom;
import view.MyColors;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainStory implements Serializable {

    // Main story breakdown:
    //                   ___                            ___
    //   ___     _____> / 2 \ ____       ___     ____> / 4 \ ____      ___       ___
    //  / 1 \ __/       \_a_/     \____ / 3 \ __/      \_a_/     \___ / 5 \ ___ / 6 \
    //  \___/   \        ___       &    \___/   \       ___      /    \___/     \___/
    //           \____> / 2 \ ____/              \___> / 4 \ ___/
    //                  \_b_/                          \_b_/
    //
    // 1 Frogmen Problem
    //   The party sets out to deal with a rampant population of frogmen.
    //   They can either attempt to find the root cause of the upheaval or
    //   simply wipe out the settlement. Either way the party discovers a Crimson Pearl
    //   in the belly of the frogmen chieftain which must have been the cause of his
    //   sudden insanity. Upon returning to the provider of the quest, the party finds
    //   out that their promised reward has been squandered, and they must travel to
    //   the nearest castle to claim the money.

    // 2 (a) Rescue Mission
    //   The party gains the reward for (1) from the castle's lord. The lord is impressed
    //   by the party's work and would like to hire them for a rescue mission. His master-at-arms
    //   and loyal advisor, Caid is missing and the party must navigate the darker districts to find him. Once
    //   they find him, they realized he hasn't been kidnapped, he's actually just had enough of
    //   being in the lords employ.

    // 2 (b) Deliver a Package
    //   The party seeks out a local witch who knows more about the Crimson Pearl.
    //   The witch promises to tell all she knows about the pearl, if the party delivers
    //   a package for her. Seems easy enough, but there are others who are interested in
    //   the contents of the package, ready to intercept the party en route.
    //   Once completed, the Witch explains that the Crimson Pearl is a sorcerer's tool
    //   for dominating somebody's mind. But how did it end up in a frogman's belly?
    //   Pearls like these haven't been made for centuries, and only by a secluded cult of
    //   sorcerer's known as the Cordial Quad. It is the general belief that they were all wiped out centuries ago.
    //   The party should inform the proper authorities about this.

    // 3 Research the Cordial Quad (Requires both 2a and 2b completed)
    //   With the help of the lords court mage, the party is tasked with researching the cordial quad. What happened
    //   to them? Where were they located, where did they go? Are they still active?
    //   The party must travel to another town to visit a library to find the answers to these questions.
    //   In the library, the party meets Willis, a learned historian and mage, who helps them.

    // 4 (a) Willis's Quest (Possibility to recruit Willis)

    // 4 (b) Caid's Quest (Possibility to recruit Caid)

    // 5 Invasion
    //   Returning to the lord with a detailed account of the Cordial Quad, but
    //   a host of evil humans are descending upon the land. The party must help the kingdom defend itself.
    //   Afterward, more crimson pearls are found in the corpses of the slain marauders.

    // 6 Distant Lands (Willis or Caid in the party, or leader at lvl 6)
    //   The party is tasked with finding the
    //   stronghold of the Cordial Quad. It is located in a distant region, and the party must travel to this
    //   faraway, and dangerous land, traverse it, and find the Quad's lair and defeat them.


    public static final Map<String, MainQuest> QUESTS = makeMainStoryQuests();

    private boolean townVisited = false;
    private boolean castleVisited = false;
    private boolean templeVisited = false;
    private List<StoryPart> storyParts = new ArrayList<>();
    private MainStorySpawnLocation spawnData;
    private GameCharacter caidCharacter = new CaidCharacter();
    private GameCharacter willisCharacter = new WillisCharacter();
    private boolean caidQuestDone;


    public MainStory() { }

    public void progressStoryForTesting(Model model) {
//        GameCharacter dummy = new GameCharacter("Dummy", "Delacroix", Race.HALF_ORC, Classes.WIT,
//                new KruskTalandro(), new CharacterClass[]{Classes.WIT, Classes.DRU, Classes.MAG, Classes.SOR});
//        setupStory(dummy);                // Get task "visit uncle"
//        getStoryParts().get(0).progress();     // Visit uncle, get Frogmen Problem quest
//        getStoryParts().get(0).progress();     // Completes frogmen problem quest
//        getStoryParts().get(0).progress();     // Returns to uncle, get visit Everix task
//        getStoryParts().get(0).transitionStep(model, 0); // Gets "Reward at ... Castle" Task
//        getStoryParts().get(0).progress();     // Visits Everix
//        getStoryParts().get(0).transitionStep(model, 1); // Gets "Find Witch" Task
//
//        getStoryParts().get(1).progress();  // Visit lord
//        getStoryParts().get(1).progress();  // Do rescue mission quest
//        getStoryParts().get(1).progress();  // Returns to lord, task completed
//        getStoryParts().get(1).progress();  //
//
//
//        getStoryParts().get(2).progress();   // Visits witch, get Special Delivery Quest
//        getStoryParts().get(2).progress();   // Completes special delivery quests
//        getStoryParts().get(2).progress();   // Returns to witch, get Crimson Pearl info -> completes task
//        getStoryParts().get(2).transitionStep(model); // Gets part three story part
//
//
//        getStoryParts().get(3).progress();  // Progressed at lord because witch part is done
//        getStoryParts().get(3).progress();  // Talks to Willis, gets trouble in the library quest
//        getStoryParts().get(3).progress();  // Completes Trouble in the Library Quest,
//        getStoryParts().get(3).progress();  // Returns to willis, cleans library and gets more info on quad.
//        getStoryParts().get(3).transitionStep(model);
//        model.setWorldState(model.getMainStory().getExpandDirection());
//
//        getStoryParts().get(4).progress(); // Get Go to Orc War Camp task
//        getStoryParts().get(4).progress(); // Quest done, return.
//        getStoryParts().get(4).progress(); // Get stuff from lord
//        addStoryPart(new ZeppelinStoryPart(model.getMainStory().getXelbiPosition(), "FJANT"));
//        getStoryParts().get(4).transitionStep(model);
//        model.getParty().addToGold(300);
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

    public void setupStory(GameCharacter whosUncle) {
        List<MainStorySpawnLocation> townsAndCastles = List.of(
                new MainStorySpawnEast(),
                new MainStorySpawnSouth(),
                new MainStorySpawnNorth(),
                new MainStorySpawnWest()
        );
        spawnData = MyRandom.sample(townsAndCastles);
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
        return spawnData != null;
    }

    public void addQuests(Model model, List<Quest> quests) {
        if (isStarted()) {
            for (StoryPart sp : storyParts) {
                sp.addQuests(model, quests);
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
        return map;
    }

    public static MainQuest getQuest(String key) {
        return QUESTS.get(key);
    }

    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        for (StoryPart sp : storyParts) {
            DailyEventState event = sp.getVisitLordEvent(model, location);
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
}
