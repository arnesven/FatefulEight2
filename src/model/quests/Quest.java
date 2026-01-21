package model.quests;

import model.Model;
import model.TimeOfDay;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SilhouetteAppearance;
import model.states.GameState;
import model.states.PayWagesState;
import model.states.QuestState;
import model.states.events.ForcedMovementEvent;
import model.states.events.MoveAwayFromCurrentPositionEvent;
import sound.BackgroundMusic;
import util.MyRandom;
import view.BorderFrame;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;
import view.subviews.ArrowMenuSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Quest {
    private static final CharacterAppearance SIL_APPEARANCE = new SilhouetteAppearance();
    private final String name;
    private final Reward reward;
    private final int moveAfter;
    private final String text;
    private final String endingText;
    private String provider;
    private final QuestDifficulty difficulty;
    private List<QuestScene> scenes;
    private List<QuestJunction> junctions;
    private QuestSuccessfulNode successEnding;
    private QuestNode failEnding;
    private List<Point> remotePath;

    public Quest(String name, String provider, QuestDifficulty difficulty,
                 Reward reward, int moveAfter, String text, String endText) {
        this.name = name;
        this.provider = provider;
        this.difficulty = difficulty;
        this.reward = reward;
        this.moveAfter = moveAfter;
        this.text = text;
        this.endingText = endText;
        resetQuest();
    }

    protected static Achievement.Data makeAchievement(Quest quest, String description) {
        return new Achievement.Data(quest.getClass().getCanonicalName(), quest.name,
                description);
    }

    protected void resetQuest() {
        successEnding = new QuestSuccessfulNode(reward, endingText);
        failEnding = new QuestFailNode();
        scenes = buildScenes();
        junctions = buildJunctions(scenes);
        connectScenesToJunctions(scenes, junctions);
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

    public final int drawQuestOfferCardMiddle(Model model, int x, int y) {
        if (moveAfter > 0) {
            BorderFrame.drawString(model.getScreenHandler(), "Move", x, y++, MyColors.WHITE, MyColors.BLACK);
            BorderFrame.drawString(model.getScreenHandler(), "  " + moveAfter, x, y++, MyColors.WHITE, MyColors.BLACK);
            y += 1;
        }
        boolean special = false;
        for (String s : getSpecialRewards()) {
            BorderFrame.drawString(model.getScreenHandler(), s, x, y++, MyColors.WHITE, MyColors.BLACK);
            special = true;
        }
        if (special) {
            y++;
        }
        return y;
    }

    protected List<String> getSpecialRewards() {
        return new ArrayList<>();
    }

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
        return endOfQuestProcedure(model, state, questWasSuccess, reward.getGold() > 0);
    }

    protected static GameState endOfQuestProcedure(Model model, QuestState state,
                                                   boolean questWasSuccess, boolean payWages) {
        state.print("Press enter to continue.");
        state.waitForReturn();
        QuestState.setCurrentTerrainSubview(model);
        model.getParty().stopHoldingQuest(state.getQuest());
        model.setTimeOfDay(TimeOfDay.EVENING);
        if (!questWasSuccess) {
            state.partyMemberSay(model.getParty().getRandomPartyMember(), MyRandom.sample(List.of(
                    "That could have gone better.",
                    "We need to do better.",
                    "Was it just bad luck?",
                    "I hope things will turn around soon.",
                    "I don't think this was my fault.",
                    "Let's not blame anybody, OK?")));
            adjustAttitudes(model, -10);
        } else if (payWages) {
            new PayWagesState(model).run(model);
        } else {
            state.partyMemberSay(model.getParty().getRandomPartyMember(), MyRandom.sample(List.of(
                    "Go us!", "This was a good experience for me.",
                    "I think we're doing pretty well.", "Times are good!",
                    "We all contributed on this one.", "Good team work everybody."
            )));
            adjustAttitudes(model, 7);
        }
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private static void adjustAttitudes(Model model, int attitude) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != model.getParty().getLeader()) {
                gc.addToAttitude(model.getParty().getLeader(), attitude);
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

    public void setRemoteLocation(Model model) {
        if (moveAfter > 0) {
            setRemotePath(model, MoveAwayFromCurrentPositionEvent.makePathToRemoteLocation(model, moveAfter));
        } else {
            setRemotePath(model, new ArrayList<>(List.of(new Point(model.getParty().getPosition()))));
        }
    }

    protected void setRemotePath(Model model, List<Point> path) {
        this.remotePath = path;
    }


    public HeldQuestData getHeldData() {
        return new HeldQuestData(getPortrait(), remotePath);
    }

    public List<Point> getRemotePath() {
        return remotePath;
    }

    public void movePartyToRemoteLocation(Model model) {
        if (moveAfter > 0) {
            List<Point> path = remotePath;
            if (model.getParty().questIsHeld(this)) {
                path = model.getParty().getHeldDataFor(this).getRemotePath();
            }
            if (path.size() > 1) {
                new ForcedMovementEvent(model, path).doTheEvent(model);
            }
        }
    }

    public boolean hasBlackCursor() {
        return false;
    }

    public boolean givesAchievement() {
        return getAchievementData() != null;
    }

    public Achievement.Data getAchievementData() {
        return null;
    }
}
