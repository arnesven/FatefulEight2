package model.quests;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.combat.TownCombatTheme;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import view.MyColors;
import view.sprites.Sprite;
import view.subviews.CombatTheme;

import java.util.List;

public class UnsuspectingLoversQuest extends Quest {

    private static final String text = "Jason and Tamara are in love with " +
            "each other, but neither knows about the " +
            "other's feelings. Their parents want a " +
            "good matchmaker to set the mood and " +
            "remove sleazy interlopers. Finally, a " +
            "perfect date will seal their love forever!";

    private static final String endText = "Jason and Tamara both thank you for bringing them together. What a happy ending!";
    private final TownCombatTheme theme;

    public UnsuspectingLoversQuest() {
        super("Unsuspecting Lovers", "a local matchmaker", QuestDifficulty.EASY, 0, 25, 50, text, endText);
        this.theme = new TownCombatTheme();
    }

    @Override
    public CombatTheme getCombatTheme() {
        return theme;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Confessions",
                List.of(new CollaborativeSkillCheckSubScene(0, 1, Skill.Persuade, 8,
                        "Okay, first step, get these two talking..."))),
                new QuestScene("Remove Interloper",
                        List.of(new SoloSkillCheckSubScene(5, 2, Skill.Persuade, 10,
                                "Perhaps we can persuade him to direct his ardor elsewhere."),
                                new ReducePartyRepCombatSubScene(4, 2))),
                new QuestScene("Date Cuisine",
                        List.of(new CollaborativeSkillCheckSubScene(3, 4, Skill.Survival, 10,
                                "I'm sure we can whip something up ourselves."),
                                new PayGoldSubScene(4, 4, 5,
                                        "We should probably cater."))),
                new QuestScene("Date Entertainment",
                        List.of(new CollaborativeSkillCheckSubScene(2, 7, Skill.Entertain, 10,
                                "How about a love song?"),
                                new PayGoldSubScene(3, 7, 5, "Can we hire a troubadour?"))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint qd1 = new QuestDecisionPoint(2, 0,
                List.of(new QuestEdge(scenes.get(1).get(0)),
                        new QuestEdge(scenes.get(1).get(1))),
                "Now, how to deal with this meddlesome interloper.");
        QuestDecisionPoint qd2 = new QuestDecisionPoint(4, 3,
                List.of(new QuestEdge(scenes.get(2).get(0)),
                        new QuestEdge(scenes.get(2).get(1))),
                "For a romantic dinner, the entrees must be supreme!");
        QuestDecisionPoint qd3 = new QuestDecisionPoint(3, 6,
                List.of(new QuestEdge(scenes.get(3).get(0)),
                        new QuestEdge(scenes.get(3).get(1))),
                        "To set the mood, some proper entertainment is required.");

        return List.of(new PauseQuestJunction(0, 0, new QuestEdge(scenes.get(0).get(0)),
                "We'll get these two lovebirds together in no time."),
                qd1, qd2, qd3);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(scenes.get(1).get(1));
        scenes.get(1).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(1).get(1).connectFail(getFailEndingNode());
        scenes.get(1).get(1).connectSuccess(junctions.get(2));

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(3));
        scenes.get(2).get(1).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectFail(getFailEndingNode());
        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectSuccess(getSuccessEndingNode());

    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    private static class ReducePartyRepCombatSubScene extends CombatSubScene {
        public ReducePartyRepCombatSubScene(int col, int row) {
            super(col, row, List.of(new InterloperEnemy('A')), true);
        }

        @Override
        protected String getCombatDetails() {
            return "an interloper infatuated with Tamara (-1 Rep)";
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.LIGHT_GREEN;
        }
    }

    private static class InterloperEnemy extends Enemy {
        private static Sprite avatar = Classes.BRD.getAvatar(Race.NORTHERN_HUMAN);

        public InterloperEnemy(char a) {
            super(a, "Interloper");
        }

        @Override
        public int getMaxHP() {
            return 4;
        }

        @Override
        public int getSpeed() {
            return 3;
        }

        @Override
        protected Sprite getSprite() {
            return avatar;
        }

        @Override
        public int getDamage() {
            return 2;
        }

        @Override
        public CombatLoot getLoot(Model model) {
            return new NoCombatLoot();
        }
    }
}
