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
                        List.of(new SkeletonCombatSubScene(2, 1),
                                new CollectiveSkillCheckSubScene(2, 0, Skill.Sneak, 5))),
                new QuestScene("Mechanical Trap",
                        List.of(new TrapSubScene(4, 3),
                                new SoloSkillCheckSubScene(3, 3, Skill.Security, 9),
                                new CollectiveSkillCheckSubScene(5, 3, Skill.Acrobatics, 4))),
                new QuestScene("Puzzle",
                        List.of(new CollaborativeSkillCheckSubScene(2, 4, Skill.Logic, 11))),
                new QuestScene("Vampire Guardian",
                        List.of(new VampireCombatSubScene(4, 7),
                        new SoloSkillCheckSubScene(6, 7, Skill.Persuade, 10))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestJunction junc0 = new QuestStartPoint(
                List.of(new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL), new QuestEdge(scenes.get(0).get(1))));
        QuestJunction junc1 = new QuestDecisionPoint(4, 2,
                List.of(new QuestEdge(scenes.get(1).get(0)),
                        new QuestEdge(scenes.get(1).get(1)),
                        new QuestEdge(scenes.get(1).get(2)),
                        new QuestEdge(scenes.get(2).get(0))));
        QuestJunction junc2 = new QuestDecisionPoint(4, 6,
                List.of(new QuestEdge(scenes.get(3).get(0)),
                        new QuestEdge(scenes.get(3).get(1))));
        return List.of(junc0, junc1, junc2,
                new SimpleJunction(4, 1, new QuestEdge(junc1)),
                new SimpleJunction(4, 4, new QuestEdge(junc2)));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(junctions.get(3));
        scenes.get(0).get(1).connectFail(scenes.get(0).get(0));
        scenes.get(0).get(1).connectSuccess(junctions.get(3));

        scenes.get(1).get(0).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);
        scenes.get(1).get(1).connectFail(scenes.get(1).get(0));
        scenes.get(1).get(1).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);
        scenes.get(1).get(2).connectFail(scenes.get(1).get(0));
        scenes.get(1).get(2).connectSuccess(junctions.get(4), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectFail(scenes.get(3).get(0));
        scenes.get(3).get(1).connectSuccess(getSuccessEndingNode());
    }

}
