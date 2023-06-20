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
import view.subviews.PortraitSubView;

import java.util.List;

public class InitialStoryPart extends StoryPart {
    private static final int NO_OF_STEPS = 4;
    public static int REWARD_GOLD = 120;
    private final String startLocation;
    private final String castleLocation;
    private final GameCharacter whosUncle;
    private final AdvancedAppearance unclePortrait;
    private final AdvancedAppearance everixPortrait;

    private int internalStep = 0;

    public InitialStoryPart(GameCharacter whosUncle) {
        List<MyPair<String, String>> townsAndCastles = List.of(
                new MyPair<>(new AshtonshireTown().getName(), new ArkvaleCastle().getName()),
                new MyPair<>(new SouthMeadhomeTown().getName(), new ArdhCastle().getName()),
                new MyPair<>(new EbonshireTown().getName(), new BogdownCastle().getName()),
                new MyPair<>(new LittleErindeTown().getName(), new SunblazeCastle().getName())
        );
        MyPair<String, String> randomPair = MyRandom.sample(townsAndCastles);
        this.startLocation = randomPair.first;
        this.castleLocation = randomPair.second;
        this.whosUncle = whosUncle;
        this.unclePortrait = PortraitSubView.makeRandomPortrait(Classes.None, whosUncle.getRace());
        this.everixPortrait = PortraitSubView.makeRandomPortrait(Classes.DRU, Race.ALL);
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new FirstMainStoryTask(startLocation, whosUncle, internalStep));
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        if (townDailyActionState.getTown().getName().equals(startLocation)) {
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
                if (loc.getName().equals(startLocation)) {
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
        return startLocation;
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
        return castleLocation;
    }

    public boolean completed() {
        return internalStep == NO_OF_STEPS;
    }
}
