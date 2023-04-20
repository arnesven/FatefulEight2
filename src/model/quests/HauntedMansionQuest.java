package model.quests;

import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.GhostEnemy;
import model.enemies.SkeletonEnemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class HauntedMansionQuest extends Quest {
    private static final String INTRO = "A nobleman is determined to move back into his ancestral home, but it has " +
            "been invaded by some spectral menace. What is the source of the dark presence, and how to exorcise it?";
    private static final String ENDING = "The nobleman is overjoyed to find his mansion purged from the dark pestilence, " +
            "and rewards you for your service,";

    public HauntedMansionQuest() {
        super("Haunted Mansion", "a nobleman", QuestDifficulty.HARD, 1, 50, 0, INTRO, ENDING);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Entrance",
                List.of(new CollaborativeSkillCheckSubScene(1, 2, Skill.Search, 10,
                        "Perhaps if we search, we can find an alternative way in."),
                        new SoloSkillCheckSubScene(2, 2, Skill.Logic, 8,
                                "There are strange markings on the door. Is this lock some kind of puzzle?"))),
                new QuestScene("Dark Hallway",
                List.of(new CollaborativeSkillCheckSubScene(6, 1, Skill.Search, 11,
                                "These hallways are like a maze. Which way should we go?"))),
                new QuestScene("Ancestral Ghosts",
                        List.of(new GhostCombatScene(4, 1, 4),
                                new GhostCombatScene(5, 4, 3),
                                new GhostCombatScene(5, 6, 2))),
                new QuestScene("Forbidding Dungeon",
                        List.of(new CollaborativeSkillCheckSubScene(3, 4, Skill.Search, 12,
                        "This place is really scary! Let's find our way out of here."))),
                new QuestScene("Secret Chamber",
                        List.of(new CollaborativeSkillCheckSubScene(4, 7, Skill.MagicWhite, 12,
                                "There is a dark curse cast upon this place. " +
                                        "We must perform a ritual to dissolve it with holy magic."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        List<QuestJunction> juncs = new ArrayList<>();
        juncs.add(new QuestStartPoint(List.of(new QuestEdge(scenes.get(0).get(0)), new QuestEdge(scenes.get(0).get(1))),
                "The door to the basement is sealed. How do we get in?"));
        SimpleJunction sj = new SimpleJunction(2, 3, new QuestEdge(scenes.get(3).get(0), QuestEdge.VERTICAL));
        juncs.add(sj);
        SimpleJunction sj2 = new SimpleJunction(5, 0, new QuestEdge(scenes.get(1).get(0)));
        juncs.add(sj2);
        SimpleJunction sj3 = new SimpleJunction(6, 3, new QuestEdge(sj));
        juncs.add(sj3);
        return juncs;
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(scenes.get(2).get(0));
        scenes.get(0).get(0).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectFail(scenes.get(2).get(0));
        scenes.get(0).get(1).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectFail(scenes.get(2).get(0));
        scenes.get(1).get(0).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);
        scenes.get(2).get(2).connectSuccess(scenes.get(4).get(0));

        scenes.get(3).get(0).connectSuccess(scenes.get(4).get(0), QuestEdge.VERTICAL);
        scenes.get(3).get(0).connectFail(scenes.get(2).get(1));

        scenes.get(4).get(0).connectFail(scenes.get(2).get(2));
        scenes.get(4).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);

    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    private static List<Enemy> makeGhosts(int num) {
        List<Enemy> result = new ArrayList<>();
        for (int i = 0; i < num; ++i) {
            result.add(new GhostEnemy('A'));
        }
        return result;
    }

    private static class GhostCombatScene extends CombatSubScene {
        private final int numberOfGhosts;

        public GhostCombatScene(int col, int row, int numberOfGhosts) {
            super(col, row, makeGhosts(numberOfGhosts), false);
            this.numberOfGhosts = numberOfGhosts;
        }

        @Override
        protected String getCombatDetails() {
            return "Ancestral Ghosts";
        }

        @Override
        public List<Enemy> getEnemies() {
            return makeGhosts(numberOfGhosts);
        }

        @Override
        protected boolean hasBeenDefeated() {
            return false;
        }
    }
}
