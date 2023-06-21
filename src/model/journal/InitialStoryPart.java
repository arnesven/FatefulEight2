package model.journal;

import model.MainStory;
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
import util.MyPair;
import util.MyRandom;
import view.ScreenHandler;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class InitialStoryPart extends StoryPart {
    private static final int NO_OF_STEPS = 4;
    public static int REWARD_GOLD = 120;
    private final MainStorySpawnLocation spawnData;
    private final GameCharacter whosUncle;
    private final AdvancedAppearance unclePortrait;
    private final AdvancedAppearance everixPortrait;

    private int internalStep = 0;

    public InitialStoryPart(GameCharacter whosUncle) {
        List<MainStorySpawnLocation> townsAndCastles = List.of(
                new MainStorySpawnLocation(new AshtonshireTown().getName(), new ArkvaleCastle().getName(), new Point(12, 8)),
                new MainStorySpawnLocation(new SouthMeadhomeTown().getName(), new ArdhCastle().getName(), new Point(12, 8)),
                new MainStorySpawnLocation(new EbonshireTown().getName(), new BogdownCastle().getName(), new Point(12, 8)),
                new MainStorySpawnLocation(new LittleErindeTown().getName(), new SunblazeCastle().getName(), new Point(12, 8))
        );
        spawnData = MyRandom.sample(townsAndCastles);
        this.whosUncle = whosUncle;
        this.unclePortrait = PortraitSubView.makeRandomPortrait(Classes.None, whosUncle.getRace());
        this.everixPortrait = PortraitSubView.makeRandomPortrait(Classes.DRU, Race.ALL);
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new FirstMainStoryTask(spawnData.getTown(), whosUncle, internalStep));
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
        if (internalStep == 1) {
            if (model.getCurrentHex().getLocation() != null && model.getCurrentHex().getLocation() instanceof TownLocation) {
                TownLocation loc = (TownLocation) model.getCurrentHex().getLocation();
                if (loc.getName().equals(spawnData.getTown())) {
                    FrogmenProblemQuest frogmen = ((FrogmenProblemQuest)MainStory.getQuest(FrogmenProblemQuest.QUEST_NAME));
                    frogmen.setPortrait(unclePortrait);
                    quests.add(frogmen);
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
        return internalStep == NO_OF_STEPS;
    }

    public Point getWitchPosition() {
        return spawnData.getWitch();
    }
}
