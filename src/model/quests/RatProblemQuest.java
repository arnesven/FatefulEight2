package model.quests;

import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.GiantRatEnemy;
import model.enemies.RatEnemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class RatProblemQuest extends Quest {
    private static final String TEXT =
            "Granny Petronella has an infestation of " +
            "rats in her basement. She's calling for " +
            "an extermination crew. Although she " +
            "has no gold to pay, she reminds you that " +
            "the merchant will happily buy all the rat " +
            "carcases you can carry.";
    private static final String END_TEXT =
            "With the rat problem dealt with you head back " +
            "to town, lugging plenty of dead rats. The " +
            "merchant indeed pays handsomely for them.";

    public RatProblemQuest() {
        super("Rat Problem", "granny Petronella", QuestDifficulty.MEDIUM, 1, 35, 0, TEXT, END_TEXT);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Rats",
                List.of(new RatCombatSubScene(6, 0, 3, false),
                        new RatCombatSubScene(5, 1, 5, false),
                        new RatCombatSubScene(4, 2, 7, false))),
                new QuestScene("Rat Poison",
                List.of(new CollaborativeSkillCheckSubScene(0, 2, Skill.SeekInfo, 10,
                        "Let's ask around the market and see if anybody is selling some rat poison"),
                        new PayGoldSubScene(1, 3, 1, "This stuff should take care of that vermin."))),
                new QuestScene("Giant Rats",
                        List.of(new RatCombatSubScene(4, 6, 3, true),
                                new RatCombatSubScene(5, 7, 5, true))),
                new QuestScene("Plant in lair",
                        List.of(new CollaborativeSkillCheckSubScene(6, 4, Skill.Logic, 10,
                                "Now we just need to figure out where to put the poison."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qs = new QuestStartPoint(List.of(new QuestEdge(scenes.get(0).get(0)),
                                                         new QuestEdge(scenes.get(1).get(0))),
                                                "How to deal with these pests?");
        return List.of(qs);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(scenes.get(0).get(2), QuestEdge.VERTICAL);
        scenes.get(0).get(2).connectSuccess(scenes.get(2).get(0), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(1).get(0).connectSuccess(scenes.get(1).get(1));
        scenes.get(1).get(1).connectSuccess(scenes.get(3).get(0));

        scenes.get(2).get(0).connectSuccess(scenes.get(2).get(1));
        scenes.get(2).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectFail(getFailEndingNode());
        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    private static class RatCombatSubScene extends CombatSubScene {
        private final int numberOfRats;
        private final boolean giant;

        public RatCombatSubScene(int col, int row, int numberOfRats, boolean isGiant) {
            super(col, row, generateEnemyList(numberOfRats, isGiant), false);
            this.numberOfRats = numberOfRats;
            this.giant = isGiant;
        }

        @Override
        protected String getCombatDetails() {
            String amount = "Some";
            if (numberOfRats > 5) {
                amount = "Lots of";
            } else if (numberOfRats > 3) {
                amount = "More";
            }
            return amount + " " + (giant?"Giant ":"") +"Rats";
        }
    }

    private static List<Enemy> generateEnemyList(int numberOfRats, boolean isGiant) {
        List<Enemy> list = new ArrayList<>();
        for (int i = numberOfRats; i > 0; --i) {
            if (isGiant) {
                list.add(new GiantRatEnemy('A'));
            } else {
                list.add(new RatEnemy('A'));
            }
        }
        return list;
    }
}
