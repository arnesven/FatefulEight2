package model.quests;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SilhouetteAppearance;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;
import view.widget.QuestBackground;

import java.util.ArrayList;
import java.util.List;

public abstract class Quest {
    private static final CharacterAppearance SIL_APPEARANCE = new SilhouetteAppearance();
    private final String name;
    private final Reward reward;
    private final String text;
    private final String endingText;
    private String provider;
    private final QuestDifficulty difficulty;
    private List<QuestScene> scenes;
    private List<QuestJunction> junctions;
    private QuestSuccessfulNode successEnding;
    private QuestNode failEnding;

    public Quest(String name, String provider, QuestDifficulty difficulty, int partyRep, int gold, int exp, String text, String endText) {
        this.name = name;
        this.provider = provider;
        this.difficulty = difficulty;
        this.reward = new Reward(partyRep, gold, exp);
        this.text = text;
        this.endingText = endText;
        resetQuest();
    }

    protected void resetQuest() {
        successEnding = new QuestSuccessfulNode(reward, endingText);
        failEnding = new QuestFailNode();
        scenes = buildScenes();
        junctions = buildJunctions(scenes);
        connectScenesToJunctions(scenes, junctions);
    }

    public Quest(String name, String provider, QuestDifficulty difficulty, int partyRep, int gold, String text, String endText) {
        this(name, provider, difficulty, partyRep, gold, 0, text, endText);
    }

    protected abstract List<QuestScene> buildScenes();

    protected abstract List<QuestJunction> buildJunctions(List<QuestScene> scenes);

    protected abstract void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions);

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public List<QuestScene> getScenes() {
        return scenes;
    }

    public List<QuestJunction> getJunctions() {
        return junctions;
    }

    public QuestJunction getStartNode() {
        return junctions.get(0);
    }

    public QuestSuccessfulNode getSuccessEndingNode() {
        return successEnding;
    }

    public QuestNode getFailEndingNode() {
        return failEnding;
    }

    public List<QuestNode> getAllNodes() {
        List<QuestNode> all = new ArrayList<>();
        for (QuestScene subScenes : scenes) {
            all.addAll(subScenes);
        }
        all.addAll(junctions);
        all.add(successEnding);
        all.add(failEnding);
        return all;
    }

    public List<QuestBackground> getBackgroundSprites() { return new ArrayList<>(); }

    public abstract MyColors getBackgroundColor();

    public CombatTheme getCombatTheme() {
        return new DungeonTheme();
    }

    public boolean drawTownOrCastleInBackground() {
        return false;
    }

    public List<QuestBackground> getDecorations() {
        return new ArrayList<>();
    }

    public Reward getReward() {
        return reward;
    }

    public CharacterAppearance getPortrait() {
        return SIL_APPEARANCE;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) { this.provider = provider; }

    public QuestDifficulty getDifficulty() {
        return difficulty;
    }

    public List<String> getDetails() {
        List<String> result = new ArrayList<>();
        for (QuestScene sc : getScenes()) {
            for (QuestSubScene qss : sc) {
                if (qss.getDetailedDescription() != null) {
                    result.add(qss.getDetailedDescription());
                }
            }
        }
        return result;
    }

    public void drawSpecialReward(Model model, int x, int y) { }

    public boolean clockEnabled() {
        return false;
    }

    public int getTimeLimitSeconds() {
        return Integer.MAX_VALUE;
    }

    public boolean clockTimeOutFailsQuest() {
        return clockEnabled();
    }

    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        return endOfQuestProcedure(model, state, questWasSuccess);
    }

    protected static GameState endOfQuestProcedure(Model model, QuestState state, boolean questWasSuccess) {
        state.print("Press enter to continue.");
        state.waitForReturn();
        QuestState.setCurrentTerrainSubview(model);
        model.getParty().stopHoldingQuest(state.getQuest());
        model.setTimeOfDay(TimeOfDay.EVENING);
        adjustAttitudes(model, questWasSuccess);
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private static void adjustAttitudes(Model model, boolean questWasSuccess) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != model.getParty().getLeader()) {
                if (questWasSuccess) {
                    gc.addToAttitude(model.getParty().getLeader(), 10);
                } else {
                    gc.addToAttitude(model.getParty().getLeader(), -10);
                }
            }
        }
    }

    public boolean arePrerequisitesMet(Model model) {
        return getPrerequisites(model) == null;
    }

    public String getPrerequisites(Model model) {
        return null;
    }

    public boolean canBeHeld() {
        return true;
    }

    public abstract BackgroundMusic getMusic();
}
