package model.quests;

import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.LizardmanEnemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class TreasureHuntQuest extends Quest {

    private static final String TEXT =
            "A junk seller has sold you a strange " +
            "map. It holds the promise of " +
            "buried treasure. It's quite the hike and " +
            "who knows what dangers lurk in the " +
            "wild?";

    private static final String END_TEXT =
            "Your journey back to town is less adventurous. " +
                    "You return happy, well rested and much " +
                    "wealthier than before.";

    public TreasureHuntQuest() {
        super("Treasure Hunt", "a junk seller", QuestDifficulty.HARD, 1, 50, 0, TEXT, END_TEXT);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Into The Wild",
                List.of(new CollectiveSkillCheckSubScene(1, 1, Skill.Survival, 4,
                                "I hope everybody is ready for a hike."),
                        new CollaborativeSkillCheckSubScene(2, 1, Skill.Survival, 8,
                                "Perhaps one of us could lead us through the wilderness?"))),
                new QuestScene("The Chilled Peaks",
                        List.of(new CollectiveSkillCheckSubScene(1, 3, Skill.Endurance, 4,
                                "Brrr! The wind is like icy knives stabbing me!"))),
                new QuestScene("The Gnat Bog",
                        List.of(new CollectiveSkillCheckSubScene(3, 3, Skill.Endurance, 3,
                                "Ouch! What do these insects live off of, when they don't get explores to feast on?"))),
                new QuestScene("Finding the Stash",
                        List.of(new CollaborativeSkillCheckSubScene(2, 4, Skill.Search, 12,
                                "There are some landmarks drawn on the map, can we find them?"),
                                new CollaborativeSkillCheckSubScene(3, 4, Skill.Logic, 10,
                                "Wait, there seems to be some clues on this map. Can anybody make sense out of this?"),
                                new CollaborativeSkillCheckSubScene(4, 4, Skill.Search, 7,
                                        "Now to find the exact, final location of the stash."))),
                new QuestScene("Lizardman Ambush", List.of(new LizardmanCombatSubScene(3, 5))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qs = new QuestStartPoint(List.of(new QuestEdge(scenes.get(0).get(0)),
                                                         new QuestEdge(scenes.get(0).get(1))),
                                                "We're heading into the wild.");
        QuestDecisionPoint qd1 = new QuestDecisionPoint(5, 1,
                List.of(new QuestEdge(scenes.get(1).get(0)), new QuestEdge(scenes.get(2).get(0))),
                "Over the mountains, or through that bog?");
        QuestDecisionPoint qd2 = new QuestDecisionPoint(0, 4,
                List.of(new QuestEdge(scenes.get(3).get(0)), new QuestEdge(scenes.get(3).get(1))),
                "Could there be more to this map, or do we just crack on?");
        SimpleJunction sj = new SimpleJunction(4, 5, new QuestEdge(scenes.get(3).get(2)));
        return List.of(qs, qd1, qd2, sj);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode());
        scenes.get(0).get(0).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(getFailEndingNode());
        scenes.get(0).get(1).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(getFailEndingNode());
        scenes.get(1).get(0).connectSuccess(junctions.get(2));

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(2));

        scenes.get(3).get(0).connectFail(scenes.get(4).get(0));
        scenes.get(3).get(0).connectSuccess(junctions.get(3));
        scenes.get(3).get(1).connectFail(scenes.get(4).get(0));
        scenes.get(3).get(1).connectSuccess(junctions.get(3));
        scenes.get(3).get(2).connectFail(getFailEndingNode());
        scenes.get(3).get(2).connectSuccess(getSuccessEndingNode());

        scenes.get(4).get(0).connectSuccess(junctions.get(3));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    private class LizardmanCombatSubScene extends CombatSubScene {
        public LizardmanCombatSubScene(int col, int row) {
            super(col, row, makeEnemies(), false);
        }

        @Override
        protected String getCombatDetails() {
            return "Lizardmen";
        }
    }

    private static List<Enemy> makeEnemies() {
        List<Enemy> list = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            list.add(new LizardmanEnemy('A'));
        }
        return list;
    }
}
