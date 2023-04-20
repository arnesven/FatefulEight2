package model.quests;

import model.Party;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.subviews.CombatTheme;
import view.subviews.DungeonTheme;
import view.widget.QuestBackground;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Quest {
    private final String name;
    private final Reward reward;
    private final String text;
    private final String provider;
    private final QuestDifficulty difficulty;
    private final List<QuestScene> scenes;
    private final List<QuestJunction> junctions;
    private final QuestSuccessfulNode successEnding;
    private final QuestNode failEnding;

    public Quest(String name, String provider, QuestDifficulty difficulty, int partyRep, int gold, int exp, String text, String endText) {
        this.name = name;
        this.provider = provider;
        this.difficulty = difficulty;
        this.reward = new Reward(partyRep, gold, exp);
        this.text = text;
        successEnding = new QuestSuccessfulNode(reward, endText);
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

    public String getProviderName() {
        return provider;
    }

    public String getBeforehandInfo() {
        return "Upon completion " + reward.getDescription() +
                " The party collectively appraises this quest to be " +
                difficulty.toString().toLowerCase() + ".";
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

    public QuestNode getSuccessEndingNode() {
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

    public void accept(Party party) {
        this.successEnding.setNumberOfStartingPartyMembers(party.size());
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
}
