package model.journal;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.map.TownLocation;
import model.map.locations.*;
import model.quests.FrogmenProblemQuest;
import model.quests.Quest;
import model.races.Race;
import model.states.dailyaction.TownDailyActionState;
import model.states.dailyaction.VisitEverixNode;
import model.states.dailyaction.VisitUncleNode;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class InitialStoryPart extends StoryPart {

    private static final int INITIAL_STEP = 0;
    private static final int DO_QUEST_STEP = 1;
    private static final int QUEST_COMPLETED_STEP = 2;
    private static final int ASK_EVERIX_STEP = 3;

    private int internalStep = 0;
    public static int REWARD_GOLD = 120;
    private final MainStorySpawnLocation spawnData;
    private final GameCharacter whosUncle;
    private final AdvancedAppearance unclePortrait;
    private final AdvancedAppearance everixPortrait;



    public InitialStoryPart(GameCharacter whosUncle) {
        List<MainStorySpawnLocation> townsAndCastles = List.of(
                new MainStorySpawnLocation(new AshtonshireTown().getName(), new ArkvaleCastle().getName(), new Point(24, 1)),
                new MainStorySpawnLocation(new SouthMeadhomeTown().getName(), new ArdhCastle().getName(), new Point(13, 11)),
                new MainStorySpawnLocation(new EbonshireTown().getName(), new BogdownCastle().getName(), new Point(5, 8)),
                new MainStorySpawnLocation(new LittleErindeTown().getName(), new SunblazeCastle().getName(), new Point(8, 11))
        );
        spawnData = MyRandom.sample(townsAndCastles);
        this.whosUncle = whosUncle;
        this.unclePortrait = PortraitSubView.makeRandomPortrait(Classes.None, whosUncle.getRace());
        this.everixPortrait = PortraitSubView.makeRandomPortrait(Classes.DRU, Race.ALL);
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new FirstMainStoryTask(spawnData.getTown(), whosUncle));
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        if (townDailyActionState.getTown().getName().equals(spawnData.getTown())) {
            int randomSeed = townDailyActionState.getTown().getName().hashCode();
            townDailyActionState.addNodeInFreeSlot(new VisitUncleNode(townDailyActionState.getTown(), this), randomSeed);
            townDailyActionState.addNodeInFreeSlot(new VisitEverixNode(this), randomSeed+1);
        }
    }

    @Override
    public void progress(int track) {
        internalStep++;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (internalStep == DO_QUEST_STEP) {
            if (model.getCurrentHex().getLocation() != null && model.getCurrentHex().getLocation() instanceof TownLocation) {
                TownLocation loc = (TownLocation) model.getCurrentHex().getLocation();
                if (loc.getName().equals(spawnData.getTown())) {
                    quests.add(getQuestAndSetPortrait(FrogmenProblemQuest.QUEST_NAME, unclePortrait,
                            whosUncle.getFirstName() + "'s Uncle"));
                }
            }
        }
    }

    @Override
    public StoryPart transition(Model model) {
        return new PartTwoStoryPart(this);
    }

    public String getStartingLocationName() {
        return spawnData.getTown();
    }

    public GameCharacter getWhosUncle() {
        return whosUncle;
    }

    public CharacterAppearance getUnclePortrait() {
        return unclePortrait;
    }

    public int getStep() {
        return internalStep;
    }

    public CharacterAppearance getEverixPortrait() {
        return everixPortrait;
    }

    public String getCastleName() {
        return spawnData.getCastle();
    }

    public boolean completed() {
        return internalStep > ASK_EVERIX_STEP;
    }

    public Point getWitchPosition() {
        return spawnData.getWitch();
    }

    public class FirstMainStoryTask extends MainStoryTask {
        private final GameCharacter whosUncle;
        private final String town;


        public FirstMainStoryTask(String startLocation, GameCharacter whosUncle) {
            super(whosUncle.getFirstName() + "'s Uncle");
            this.town = startLocation;
            this.whosUncle = whosUncle;
        }

        @Override
        public String getText() {
            switch (internalStep) {
                case INITIAL_STEP:
                    return "Visit " + whosUncle.getFirstName() + "'s uncle in the " + town +
                        ". He needs a capable group of adventurers to take care of a 'Frogmen Problem'.";
                case DO_QUEST_STEP:
                    return "Complete the '" + FrogmenProblemQuest.QUEST_NAME + "' Quest.";
                case QUEST_COMPLETED_STEP:
                    return "Return to " + whosUncle.getFirstName() + "'s uncle to claim your reward.";
                case ASK_EVERIX_STEP:
                    return "Ask Everix about the crimson orb.";
            }

            return "You helped " + whosUncle.getFirstName() + "'s uncle and the " +
                    town + " deal with the Frogmen.\n\nCompleted.";
        }

        @Override
        public boolean isComplete() {
            return internalStep > ASK_EVERIX_STEP;
        }
    }
}
