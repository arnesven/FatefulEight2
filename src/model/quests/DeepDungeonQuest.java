package model.quests;

import model.classes.Skill;
import model.quests.scenes.*;

import java.awt.*;
import java.util.List;

public class DeepDungeonQuest extends Quest {
    private static final String text = "Long regarded as forbidden by locals, this " +
            "dungeon is believed to be haunted by the " +
            "ghost of the mad Count of Vizmeria. " +
            "Recently, an antiques dealer has been " +
            "looking for a crew to clear it and bring " +
            "back an ancient magical artifact.";

    public DeepDungeonQuest() {
        super("Deep Dungeon", "an antiques dealer", QuestDifficulty.HARD, 1, 50, text);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Skeleton Sentries",
                        List.of(new SkeletonCombatSubScene(1, 1),
                                new CollectiveSkillCheckSubScene(2, 1, Skill.Sneak, 5))),
                new QuestScene("Mechanical Trap",
                        List.of(new TrapSubScene(1, 3),
                                new SoloSkillCheckSubScene(2, 3, Skill.Security, 9),
                                new CollectiveSkillCheckSubScene(3, 3, Skill.Acrobatics, 4))),
                new QuestScene("Puzzle",
                        List.of(new CollaborativeSkillCheckSubScene(5, 3, Skill.Logic, 11))),
                new QuestScene("Vampire Guardian",
                        List.of(new VampireCombatSubScene(1, 5),
                        new SoloSkillCheckSubScene(2, 5, Skill.Persuade, 10))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPoint(
                List.of(new QuestEdge(scenes.get(0).get(0)), new QuestEdge(scenes.get(0).get(1)))),
                new QuestDecisionPoint(5, 1,
                        List.of(new QuestEdge(scenes.get(1).get(0)),
                                new QuestEdge(scenes.get(1).get(1)),
                                new QuestEdge(scenes.get(1).get(2)),
                                new QuestEdge(scenes.get(2).get(0)))),
                new QuestDecisionPoint(5, 5,
                        List.of(new QuestEdge(scenes.get(3).get(0)),
                                new QuestEdge(scenes.get(3).get(1)))));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(new QuestEdge(junctions.get(1)));
        scenes.get(0).get(1).connectFail(new QuestEdge(scenes.get(0).get(0)));
        scenes.get(0).get(1).connectSuccess(new QuestEdge(junctions.get(1)));

        scenes.get(1).get(0).connectSuccess(new QuestEdge(junctions.get(2)));
        scenes.get(1).get(1).connectFail(new QuestEdge(scenes.get(1).get(0)));
        scenes.get(1).get(1).connectSuccess(new QuestEdge(junctions.get(2)));
        scenes.get(1).get(2).connectFail(new QuestEdge(scenes.get(1).get(0)));
        scenes.get(1).get(2).connectSuccess(new QuestEdge(junctions.get(2)));

        scenes.get(2).get(0).connectFail(new QuestEdge(getFailEndingNode()));
        scenes.get(2).get(0).connectSuccess(new QuestEdge(junctions.get(2)));

        scenes.get(3).get(0).connectSuccess(new QuestEdge(getSuccessEndingNode()));
        scenes.get(3).get(1).connectFail(new QuestEdge(scenes.get(3).get(0)));
        scenes.get(3).get(1).connectSuccess(new QuestEdge(getSuccessEndingNode()));
    }

}
