package model;

import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.KruskTalandro;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.journal.*;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.quests.*;
import model.races.Race;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.InitialLeadsEveningState;
import model.states.dailyaction.TownDailyActionState;

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
    private StoryPart currentStoryPart = null;
    private InitialStoryPart firstStoryPart;

    public MainStory() {
        // TODO: This is just test stuff
//        GameCharacter dummy = new GameCharacter("Dummy", "Delacroix", Race.HALF_ORC, Classes.WIT,
//                new KruskTalandro(), new CharacterClass[]{Classes.WIT, Classes.DRU, Classes.MAG, Classes.SOR});
//        firstStoryPart = new InitialStoryPart(dummy);
//        firstStoryPart.progress();
//        firstStoryPart.progress();
//        firstStoryPart.progress();
//        firstStoryPart.progress();
//        currentStoryPart = new PartTwoStoryPart(firstStoryPart);
//        currentStoryPart.progress(StoryPart.TRACK_B);
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
            if (currentStoryPart != null) {
                result.addAll(currentStoryPart.getJournalEntries());
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
        this.firstStoryPart = new InitialStoryPart(whosUncle);
        currentStoryPart = firstStoryPart;
    }

    public TownLocation getStartingLocation(Model model) {
        return model.getWorld().getTownByName(firstStoryPart.getStartingLocationName());
    }

    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        townVisited = true;
        if (isStarted()) {
            currentStoryPart.handleTownSetup(townDailyActionState);
        }
    }

    public boolean isStarted() {
        return currentStoryPart != null;
    }

    public void increaseStep(Model model, int track) {
        if (isStarted()) {
            currentStoryPart.progress(track);
            JournalEntry.printJournalUpdateMessage(model);
        }
    }

    public void increaseStep(Model model) {
        increaseStep(model, StoryPart.TRACK_A);
    }

    public void transitionStep(Model model) {
        if (isStarted()) {
            currentStoryPart = currentStoryPart.transition(model);
        }
    }

    public void addQuests(Model model, List<Quest> quests) {
        if (isStarted()) {
            currentStoryPart.addQuests(model, quests);
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
        return map;
    }

    public static MainQuest getQuest(String key) {
        return QUESTS.get(key);
    }

    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        return currentStoryPart.getVisitLordEvent(model, location);
    }

    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        if (isStarted()) {
            currentStoryPart.drawMapObjects(model, x, y, screenX, screenY);
        }
    }

    public List<DailyAction> getDailyActionsForHex(Model model, WorldHex worldHex) {
        return currentStoryPart.getDailyActions(model, worldHex);
    }
}
