package model.journal;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.map.TownLocation;
import model.quests.FrogmenProblemQuest;
import model.quests.Quest;
import model.races.Race;
import model.states.dailyaction.TownDailyActionState;
import model.states.dailyaction.VisitEverixNode;
import model.states.dailyaction.VisitUncleNode;
import view.subviews.PortraitSubView;

import java.util.List;

public class InitialStoryPart extends StoryPart {

    private static final int INITIAL_STEP = 0;
    private static final int DO_QUEST_STEP = 1;
    private static final int QUEST_COMPLETED_STEP = 2;
    private static final int ASK_EVERIX_STEP = 3;
    private final String townName;

    private int internalStep = 0;
    public static int REWARD_GOLD = 120;
    private final GameCharacter whosUncle;
    private final CharacterAppearance unclePortrait;
    private final AdvancedAppearance everixPortrait;
    private WitchStoryPart witchPart = null;

    public InitialStoryPart(GameCharacter whosUncle, String townName) {
        this.whosUncle = whosUncle;
        this.townName = townName;
        this.unclePortrait = PortraitSubView.makeOldPortrait(Classes.None, whosUncle.getRace(), false);
        this.everixPortrait = PortraitSubView.makeRandomPortrait(Classes.DRU, Race.ALL, true);
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new FirstMainStoryTask(townName, whosUncle));
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {
        if (townDailyActionState.getTown().getName().equals(townName)) {
            int randomSeed = townDailyActionState.getTown().getName().hashCode();
            townDailyActionState.addNodeInFreeSlot(new VisitUncleNode(townDailyActionState.getTown(), this), randomSeed);
            townDailyActionState.addNodeInFreeSlot(new VisitEverixNode(this), randomSeed+1);
        }
    }

    @Override
    public void progress() {
        internalStep++;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (internalStep == DO_QUEST_STEP) {
            if (model.getCurrentHex().getLocation() != null && model.getCurrentHex().getLocation() instanceof TownLocation) {
                TownLocation loc = (TownLocation) model.getCurrentHex().getLocation();
                if (loc.getName().equals(townName)) {
                    quests.add(getQuestAndSetPortrait(FrogmenProblemQuest.QUEST_NAME, unclePortrait,
                            whosUncle.getFirstName() + "'s Uncle"));
                }
            }
        }
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        if (witchPart == null) {
            witchPart = new WitchStoryPart(model.getMainStory().getWitchPosition(), model.getMainStory().getCastleName());
        }
        if (track == 0) {
            return new RescueMissionStoryPart(witchPart, model.getMainStory().getCastleName(), model.getMainStory().getLibraryTownName());
        }
        return witchPart;
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

    @Override
    public boolean isCompleted() {
        return internalStep > ASK_EVERIX_STEP;
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
                    return "Complete the '" + FrogmenProblemQuest.QUEST_NAME + "' Quest (in the " + town + ").";
                case QUEST_COMPLETED_STEP:
                    return "Return to " + whosUncle.getFirstName() + "'s uncle in the " + town + ", to claim your reward.";
                case ASK_EVERIX_STEP:
                    return "Ask Everix in the " + town + " about the crimson orb.";
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
