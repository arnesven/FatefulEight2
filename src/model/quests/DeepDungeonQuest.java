package model.quests;

import model.classes.Skill;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.SkeletonCombatSubScene;

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
                                new CollectiveSkillCheckSubScene(2, 1, Skill.Sneak, 5))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPoint(List.of(scenes.get(0).get(0), scenes.get(0).get(1))));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(getSuccessEndingNode());
        scenes.get(0).get(1).connectFail(scenes.get(0).get(0));
        scenes.get(0).get(1).connectSuccess(getSuccessEndingNode());
    }

}
