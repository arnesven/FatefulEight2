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

    // 2 (a) Deliver a Package
    //   The party seeks out a local witch who knows more about the Crimson Pearl.
    //   The witch promises to tell all she knows about the pearl, if the party delivers
    //   a package for her. Seems easy enough, but there are others who are interested in
    //   the contents of the package, ready to intercept the party en route.
    //   Once completed, the Witch explains that the Crimson Pearl is a sorcerer's tool
    //   for dominating somebody's mind. But how did it end up in a frogman's belly?
    //   Pearls like these haven't been made for centuries, and only by a secluded cult of
    //   sorcerer's known as the Cordial Quad. It is the general belief that they were all wiped out centuries ago.
    //   The party should inform the proper authorities about this.

    // 2 (b) Rescue Mission
    //   The party gains the reward for (1) from the castle's lord. The lord is impressed
    //   by the party's work and would like to hire them for a rescue mission. His master-at-arms
    //   and loyal advisor, Cade is missing and the party must navigate the darker districts to find him. Once
    //   they find him, they realized he hasn't been kidnapped, he's actually just had enough of
    //   being in the lords employ.

    // 3 Research the Cordial Quad (Requires both 2a and 2b completed)
    //   With the help of the lords court mage, the party is tasked with researching the cordial quad. What happened
    //   to them? Where were they located, where did they go? Are they still active?
    //   The party must travel to another town to visit a library to find the answers to these questions.
    //   In the library, the party meets Willis, a learned historian and mage, who helps them.

    // 4 (a) Willis's Quest (Possibility to recruit Willis)

    // 4 (b) Cade's Quest (Possibility to recruit Cade)

    // 5 Invasion
    //   Returning to the lord with a detailed account of the Cordial Quad, but
    //   a host of evil humans are descending upon the land. The party must help the kingdom defend itself.
    //   Afterward, more crimson pearls are found in the corpses of the slain marauders.

    // 6 Distant Lands (Willis or Cade in the party, or leader at lvl 6)
    //   The party is tasked with finding the
    //   stronghold of the Cordial Quad. It is located in a distant region, and the party must travel to this
    //   faraway, and dangerous land, traverse it, and find the Quad's lair and defeat them.



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
        if (townDailyActionState.getTown().getName().equals(startLocation)) {
            int randomSeed = townDailyActionState.getTown().getName().hashCode();
            townDailyActionState.addNodeInFreeSlot(new VisitUncleNode(townDailyActionState.getTown(), whosUncle, unclePortrait), randomSeed);
        }
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
