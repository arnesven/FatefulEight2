package model.quests;

import model.Model;
import model.classes.Skill;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.states.QuestState;
import view.MyColors;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class DefendTheVillageQuest extends Quest {
    private static final String TEXT =
            "Some peasants are looking for capable warriors " +
            "to come and defend their village from a raiding " +
                    "Orcish band.";
    private static final String END_TEXT =
            "The peasants thank you profusely and hope that " +
                    "you will return some day soon.";

    public DefendTheVillageQuest() {
        super("Defend the Village", "some desperate peasants", QuestDifficulty.HARD,
                1, 50, 0, TEXT, END_TEXT);
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new GrassCombatTheme();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Train Villagers",
                List.of(new SoloSkillCheckSubScene(6, 1, Skill.Leadership, 8,
                        "What's needed here is a speech to inspire some courage!"),
                        new SoloSkillCheckSubScene(4, 1, Skill.Polearms, 7,
                                "Every man and woman who can fight, grab a spear!"))),
                new QuestScene("Fortifications",
                List.of(new CollaborativeSkillCheckSubScene(4, 5, Skill.Security, 10,
                                "With some proper locks, we can just shut the bandits out."),
                        new CollaborativeSkillCheckSubScene(4, 3, Skill.Labor, 11,
                                "Gather all the wood you can, and some nails. Follow me. We're building some barricades."))),
                new QuestScene("Raise Morale",
                List.of(new CollaborativeSkillCheckSubScene(1, 5, Skill.Entertain, 9,
                        "Now all we can do is wait. Or perhaps, we could have some fun?"))),
                new QuestScene("Battle", List.of(new VariableBanditCombatSubScene(3, 7))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qss = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(0).get(1)),
                new QuestEdge(scenes.get(3).get(0), QuestEdge.VERTICAL)),
                "Those bandits will be coming soon. How to prepare the peasants?");
        QuestDecisionPoint qd1 = new QuestDecisionPoint(5, 2,
                List.of(new QuestEdge(scenes.get(1).get(0), QuestEdge.VERTICAL),
                        new QuestEdge(scenes.get(1).get(1), QuestEdge.VERTICAL)),
                "Can we improve the fortifications?");
        CountingJunction cnt1 = new CountingJunction(5, 1, new QuestEdge(qd1));
        SimpleJunction sj1 = new SimpleJunction(3, 4, new QuestEdge(scenes.get(2).get(0)));
        CountingJunction cnt2 = new CountingJunction(4, 4, new QuestEdge(sj1));
        SimpleJunction sj2 = new SimpleJunction(2, 6, new QuestEdge(scenes.get(3).get(0)));
        CountingJunction cnt3 = new CountingJunction(1, 6, new QuestEdge(sj2));
        return List.of(qss, cnt1, qd1, sj1, cnt2, sj2, cnt3);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(junctions.get(3));
        scenes.get(1).get(0).connectSuccess(junctions.get(4));
        scenes.get(1).get(1).connectFail(junctions.get(3));
        scenes.get(1).get(1).connectSuccess(junctions.get(4));

        scenes.get(2).get(0).connectFail(junctions.get(5));
        scenes.get(2).get(0).connectSuccess(junctions.get(6));

        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    private static class VariableBanditCombatSubScene extends CombatSubScene {
        public VariableBanditCombatSubScene(int col, int row) {
            super(col, row, List.of(new BanditEnemy('A', "Bandit", 5)));
        }

        @Override
        protected String getCombatDetails() {
            return "bandits";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            int[] numEnemies = new int[]{9, 6, 4, 2};
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < numEnemies[state.getCounter()]; ++i) {
                enemies.add(new BanditEnemy('A', "Bandit", 5));
            }
            super.setEnemies(enemies);
            return super.run(model, state);
        }
    }
}
