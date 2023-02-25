package model.quests;

import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.LizardmanEnemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import view.MyColors;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private List<QuestBackground> bgSprites = makeBackgroundSprites();

    public TreasureHuntQuest() {
        super("Treasure Hunt", "a junk seller", QuestDifficulty.HARD, 1, 50, 0, TEXT, END_TEXT);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Into The Wild",
                List.of(new CollectiveSkillCheckSubScene(1, 0, Skill.Survival, 4,
                                "I hope everybody is ready for a hike."),
                        new CollaborativeSkillCheckSubScene(1, 1, Skill.Survival, 8,
                                "Perhaps one of us could lead us through the wilderness?"))),
                new QuestScene("The Chilled Peaks",
                        List.of(new CollectiveSkillCheckSubScene(7, 2, Skill.Endurance, 4,
                                "Brrr! The wind is like icy knives stabbing me!"))),
                new QuestScene("The Gnat Bog",
                        List.of(new CollectiveSkillCheckSubScene(5, 2, Skill.Endurance, 3,
                                "Ouch! What do these insects live off of, when they don't get adventurers to feast on?"))),
                new QuestScene("Finding the Stash",
                        List.of(new CollaborativeSkillCheckSubScene(2, 5, Skill.Search, 12,
                                "There are some landmarks drawn on the map, can we find them?"),
                                new CollaborativeSkillCheckSubScene(3, 5, Skill.Logic, 10,
                                "Wait, there seems to be some clues on this map. Can anybody make sense out of this?"),
                                new CollaborativeSkillCheckSubScene(4, 7, Skill.Search, 7,
                                        "Now to find the exact, final location of the stash."))),
                new QuestScene("Lizardman Ambush", List.of(new LizardmanCombatSubScene(3, 6))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qs = new QuestStartPoint(List.of(new QuestEdge(scenes.get(0).get(0)),
                                                         new QuestEdge(scenes.get(0).get(1), QuestEdge.VERTICAL)),
                                                "We're heading into the wild.");
        QuestDecisionPoint qd1 = new QuestDecisionPoint(3, 0,
                List.of(new QuestEdge(scenes.get(1).get(0)), new QuestEdge(scenes.get(2).get(0))),
                "Over the mountains, or through that bog?");
        QuestDecisionPoint qd2 = new QuestDecisionPoint(3, 4,
                List.of(new QuestEdge(scenes.get(3).get(0)), new QuestEdge(scenes.get(3).get(1))),
                "Could there be more to this map, or do we just crack on?");
        SimpleJunction sj = new SimpleJunction(4, 6, new QuestEdge(scenes.get(3).get(2)));
        return List.of(qs, qd1, qd2, sj);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(getFailEndingNode());
        scenes.get(1).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectFail(scenes.get(4).get(0), QuestEdge.VERTICAL);
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
    public CombatTheme getCombatTheme() {
        return new GrassCombatTheme();
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }

    private List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> result = new ArrayList<>();
        Random rand = new Random(1234);
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                result.add(new QuestBackground(new Point(col, row),
                        GrassCombatTheme.grassSprites[rand.nextInt(GrassCombatTheme.grassSprites.length)]));
            }
        }
        Sprite32x32 bogSprite = new Sprite32x32("bogsprite", "quest.png", 0x65,
                MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.TAN, MyColors.GREEN);
        Sprite32x32 hills = new Sprite32x32("hillssprite", "quest.png", 0x67,
                MyColors.BLACK, MyColors.GREEN, MyColors.GREEN, MyColors.GREEN);
        Sprite32x32 mountains = new Sprite32x32("mountains", "quest.png", 0x66,
                MyColors.BLACK, MyColors.WHITE, MyColors.GRAY, MyColors.GREEN);
        for (int row = 2; row < 4; ++row) {
            result.add(new QuestBackground(new Point(4, row), bogSprite));
            result.add(new QuestBackground(new Point(5, row), bogSprite));
            result.add(new QuestBackground(new Point(6, row), hills));
            result.add(new QuestBackground(new Point(7, row), mountains));
        }

        result.add(new QuestBackground(new Point(2, 7), hills));
        result.add(new QuestBackground(new Point(3, 6), hills));
        result.add(new QuestBackground(new Point(5, 6), hills));
        result.add(new QuestBackground(new Point(4, 8), hills));
        result.add(new QuestBackground(new Point(5, 8), hills));

        for (int i = 0; i < 3; ++i) {
            result.add(new QuestBackground(new Point(i, 3), hills));
            result.add(new QuestBackground(new Point(i, 4), mountains));
            result.add(new QuestBackground(new Point(i, 5), hills));
        }
        result.add(new QuestBackground(new Point(3, 4), mountains));
        return result;
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    private static class LizardmanCombatSubScene extends CombatSubScene {
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
