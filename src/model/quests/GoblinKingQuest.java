package model.quests;

import model.Model;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.enemies.GoblinKingEnemy;
import model.enemies.GoblinSpearman;
import model.quests.scenes.CombatSubScene;
import model.ruins.DungeonChest;
import model.states.QuestState;
import view.sprites.DungeonWallSprite;
import view.MyColors;
import view.sprites.FirePlaceSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GoblinKingQuest extends Quest {
    private static final String INTRO = "Once more reunited, the party heads for the throne room of Khaz-Ak-Ahrak. " +
            "Once the seat of the splendid Goblin King Gwurin, it is now defiled by the wretched Goblin King " +
            "Aboz and his royal guards.";
    private static final String ENDING = "With Aboz slain and his remaining minions scattered the party heaves " +
            "a sigh of relief. Now dwarves may once again call this place their home. You however, head for the " +
            "sanctuary of the nearest tavern to drain a well-earned tankard of ale.";
    private static final Sprite FLOOR = new Sprite32x32("townhallfloor", "world_foreground.png", 0x56,
            MyColors.BLACK, MyColors.GRAY, MyColors.TAN);
    private static final Sprite FIREPLACE = new FirePlaceSprite();
    private static final List<QuestBackground> BACKGROUND = makeBackground();
    private static final List<QuestBackground> DECORATIONS = makeDecorations();

    private List<List<Enemy>> waves;

    private List<Enemy> currentEnemySet;

    public GoblinKingQuest() {
        super("Goblin King", "Nobody", QuestDifficulty.MEDIUM, 2, 300, 0, INTRO, ENDING);
        waves = new ArrayList<>();
        waves.add(new ArrayList<>(List.of(new GoblinKingEnemy('A'), new GoblinSpearman('B'),
                new GoblinSpearman('B'), new GoblinSpearman('B'), new GoblinSpearman('B'))));
        waves.add(new ArrayList<>(List.of(new GoblinSpearman('B'), new GoblinSpearman('B'),
                new GoblinSpearman('B'))));
        waves.add(new ArrayList<>(List.of(new GoblinSpearman('B'), new GoblinSpearman('B'),
                new GoblinSpearman('B'))));
        currentEnemySet = new ArrayList<>();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Final Battle", List.of(
                new TimedCombatScene(3, 2, 0, List.of(new GoblinKingEnemy('A')), "Goblin King and Guards"),
                new TimedCombatScene(2, 4, 1, List.of(new GoblinSpearman('B')), "Reinforcements"),
                new AbozDeadConditionSubScene(4, 5),
                new TimedCombatScene(5, 3, 2, List.of(new GoblinSpearman('B')), "More Reinforcements")
        )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)), "There he is, Aboz... Lets get him!"));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(0).get(1), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(scenes.get(0).get(2), QuestEdge.VERTICAL);
        scenes.get(0).get(2).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(2).connectFail(scenes.get(0).get(3));
        scenes.get(0).get(3).connectSuccess(scenes.get(0).get(2));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND;
    }

    @Override
    public List<QuestBackground> getDecorations() {
        return DECORATIONS;
    }

    private class TimedCombatScene extends CombatSubScene {
        private final String details;
        private final int wave;

        public TimedCombatScene(int col, int row, int wave, List<Enemy> toShow, String details) {
            super(col, row, toShow);
            setTimeLimit(2);
            this.wave = wave;
            this.details = details;
        }

        @Override
        protected String getCombatDetails() {
            return details;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            currentEnemySet.removeIf(Combatant::isDead);
            currentEnemySet.addAll(waves.get(wave));
            setEnemies(currentEnemySet);
            return super.run(model, state);
        }
    }

    private class AbozDeadConditionSubScene extends ConditionSubScene {
        public AbozDeadConditionSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        public String getDescription() {
            return "Aboz Defeated?";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            if (waves.get(0).get(0).isDead()) {
                state.println("The goblins suddenly throw down their weapons and flee like mad.");
                model.getParty().randomPartyMemberSay(model, List.of("Hey! Come back! Cowards!"));
                state.leaderSay( "Not so eager to fight when their king is dead.");
                model.getParty().randomPartyMemberSay(model, List.of("And neither am I. Let's get out of here, " +
                        "I've had enough of these stinking tunnels"));
                state.leaderSay("Sure. Let's just help ourselves to this treasure hoard first!");
                model.getLog().waitForAnimationToFinish();
                return getSuccessEdge();
            }
            state.printQuote("Aboz", "Common lads! To me! To me! Let's show these uninvited guests " +
                    "some true goblin hospitality.");
            state.println("Suddenly, more goblins fill the hall and rush toward the party.");
            model.getLog().waitForAnimationToFinish();
            return getFailEdge();
        }
    }

    private static List<QuestBackground> makeBackground() {
        List<QuestBackground> list = new ArrayList<>();
        list.add(new QuestBackground(new Point(0, 0), AbandonedMineQuest.LL_CORNER, false));
        for (int i = 1; i < 3; ++i) {
            list.add(new QuestBackground(new Point(i, 0), AbandonedMineQuest.HORIZONTAL, false));
        }
        list.add(new QuestBackground(new Point(3, 0), AbandonedMineQuest.UR_CORNER, false));
        list.add(new QuestBackground(new Point(3, 1), DungeonWallSprite.DOOR, false));
        list.add(new QuestBackground(new Point(2, 1), DungeonWallSprite.UPPER_WALL, false));
        list.add(new QuestBackground(new Point(4, 1), DungeonWallSprite.UPPER_WALL, false));
        list.add(new QuestBackground(new Point(5, 1), DungeonWallSprite.UPPER_WALL, false));

        for (int i = 1; i < 7; ++i) {
            list.add(new QuestBackground(new Point(1, i), DungeonWallSprite.FULL_WALL, false));
            list.add(new QuestBackground(new Point(6, i), DungeonWallSprite.FULL_WALL, false));
        }

        list.add(new QuestBackground(new Point(2, 6), DungeonWallSprite.FULL_WALL, false));
        list.add(new QuestBackground(new Point(3, 6), DungeonWallSprite.FULL_WALL, false));
        list.add(new QuestBackground(new Point(4, 6), DungeonWallSprite.DOOR, false));
        list.add(new QuestBackground(new Point(5, 6), DungeonWallSprite.FULL_WALL, false));
        list.add(new QuestBackground(new Point(4, 7), AbandonedMineQuest.VERTICAL, false));
        list.add(new QuestBackground(new Point(4, 8), AbandonedMineQuest.LL_CORNER, false));
        list.add(new QuestBackground(new Point(5, 8), AbandonedMineQuest.HORIZONTAL, false));
        list.add(new QuestBackground(new Point(6, 8), AbandonedMineQuest.HORIZONTAL, false));
        list.add(new QuestBackground(new Point(7, 8), AbandonedMineQuest.ENTRANCE, false));

        for (int y = 2; y < 6; ++y) {
            for (int x = 2; x < 6; ++x) {
                list.add(new QuestBackground(new Point(x, y), FLOOR, false));
            }
        }

        return list;
    }


    private static List<QuestBackground> makeDecorations() {
        List<QuestBackground> list = new ArrayList<>();
        list.add(new QuestBackground(new Point(5, 2), DungeonChest.BIG_CHEST_CLOSED, false));
        list.add(new QuestBackground(new Point(2, 2), FIREPLACE, false));
        return list;
    }
}
