package model.quests;

import model.Model;
import model.classes.Skill;
import model.enemies.BanditEnemy;
import model.enemies.BearEnemy;
import model.enemies.Enemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x16;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MissingBrotherQuest extends Quest {
    private static final String text = "Ghania hasn't heard from her brother " +
            "in months. He traveled west to find his " +
            "fortune but may have run into bad luck " +
            "instead. She offers a handsome reward " +
            "for finding him.";

    private static final String endText = "You accompany Ghania's brother back to town " +
            "and she rewards you for returning him safely.";
    private static List<QuestBackground> bgSprites = makeBackgroundSprites();

    public MissingBrotherQuest() {
        super("Missing Brother", "Ghania", QuestDifficulty.MEDIUM, 1, 35, 0, text, endText);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Travel West",
                List.of(new CollectiveSkillCheckSubScene(0, 1, Skill.Survival, 3,
                        "We're trekking through the wilderness!"),
                        new PayGoldSubScene(7, 0, 5,
                                "Why walk when we can ride?"))),
                new QuestScene("Wild Beasts",
                        List.of(new WildBeastsCombatSubScene(3, 3),
                                new SoloSkillCheckSubScene(1, 3, Skill.Survival, 9,
                                        "Maybe someone knows how to calm these beasts down?"),
                                new CollectiveSkillCheckSubScene(5, 3, Skill.Sneak, 4,
                                        "Maybe if we are very, very, quiet..."))),
                new QuestScene("Follow Leads",
                        List.of(new CollaborativeSkillCheckSubScene(6, 5, Skill.SeekInfo, 7,
                                "Let's ask around for Ghania's Brother."))),
                new QuestScene("Bandit Camp",
                        List.of(new BanditCampCombat(2, 7),
                                new SoloSkillCheckSubScene(3, 7, Skill.Sneak, 7,
                                        "It could be possible to sneak into the camp and rescue Ghania's Brother."),
                                new PayGoldSubScene(4, 7, 5,
                                        "Let's just pay the ransom for Ghania's Brother."))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint start = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(0).get(1))),
                "We're on the brother's trail.");

        QuestDecisionPoint qd1 = new QuestDecisionPoint(5, 2,
        List.of(new QuestEdge(scenes.get(1).get(0)),
                new QuestEdge(scenes.get(1).get(1)),
                new QuestEdge(scenes.get(1).get(2))),
                "Looks like we're going to get the full experience of the local fauna.");

        SimpleJunction sj = new SimpleJunction(5, 4, new QuestEdge(scenes.get(2).get(0)));

        QuestDecisionPoint qd3 = new QuestDecisionPoint(4, 6,
                List.of(new QuestEdge(scenes.get(3).get(0)),
                        new QuestEdge(scenes.get(3).get(1)),
                        new QuestEdge(scenes.get(3).get(2))),
                "Ghania's brother is being held in the bandit camp!");

        return List.of(start, qd1, sj, qd3);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectSuccess(scenes.get(2).get(0), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(1).get(1).connectFail(scenes.get(1).get(0));
        scenes.get(1).get(1).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(1).get(2).connectFail(scenes.get(1).get(0));
        scenes.get(1).get(2).connectSuccess(junctions.get(2), QuestEdge.VERTICAL);

        scenes.get(2).get(0).connectFail(getFailEndingNode());
        scenes.get(2).get(0).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectFail(scenes.get(3).get(0));
        scenes.get(3).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(2).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new GrassCombatTheme();
    }

    private static class WildBeastsCombatSubScene extends CombatSubScene {
        public WildBeastsCombatSubScene(int col, int row) {
            super(col, row, List.of(new BearEnemy('A'), new BearEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "wild beasts";
        }
    }

    private static class BanditCampCombat extends CombatSubScene {
        private static final List<Enemy> bandits = makeBandits();

        public BanditCampCombat(int col, int row) {
            super(col, row, bandits);
        }

        @Override
        protected String getCombatDetails() {
            return "bandits";
        }


        private static List<Enemy> makeBandits() {
            List<Enemy> list = new ArrayList<>();
            for (int i = 0; i < 7; ++i) {
                list.add(new BanditEnemy('A', "Bandit", 4));
            }
            return list;
        }
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> result = new ArrayList<>();
        Random rand = new Random(1234);
        for (int row = 1; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                if (row != 8) {
                    result.add(new QuestBackground(new Point(col, row),
                            GrassCombatTheme.grassSprites[rand.nextInt(GrassCombatTheme.grassSprites.length)]));
                }
            }
        }
        final Sprite townSprite = new Sprite32x32("townspriteqmb", "quest.png", 0x51,
                MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GRAY, MyColors.GREEN);
        result.add(new QuestBackground(new Point(7, 6), townSprite));
        final Sprite woods = new Sprite32x32("woodsqmb", "quest.png", 0x53,
                MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.GREEN);
        result.add(new QuestBackground(new Point(6, 2), woods, true));
        result.add(new QuestBackground(new Point(6, 3), woods, true));
        final Sprite woodsHalf = new Sprite32x32("woodshalfqmb", "quest.png", 0x54,
                MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.GREEN);
        for (int i = 1; i < 6; ++i) {
            result.add(new QuestBackground(new Point(i, 1), woodsHalf, true));
            if (i != 5) {
                result.add(new QuestBackground(new Point(i, 2), woodsHalf, true));
            }
            result.add(new QuestBackground(new Point(i, 5), woodsHalf, true));
        }
        result.add(new QuestBackground(new Point(6, 1), woodsHalf, true));
        result.add(new QuestBackground(new Point(6, 4), woods, true));
        final Sprite woodsCorr = new Sprite32x32("woodscorrqmb", "quest.png", 0x55,
                MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.GREEN);
        for (int i = 2; i < 9; ++i) {
            result.add(new QuestBackground(new Point(0, i), woodsCorr, true));
        }
        final Sprite camp = new Sprite32x32("campqmb", "quest.png", 0x56,
                MyColors.BLACK, MyColors.BROWN, MyColors.TAN, MyColors.GREEN);
        result.add(new QuestBackground(new Point(5,7), camp, false));
        final Sprite horseCart = new Sprite32x32("horsecart", "quest.png", 0x57,
                MyColors.BROWN, MyColors.GRAY, MyColors.TAN, MyColors.GREEN);
        result.add(new QuestBackground(new Point(1,0), horseCart, true));

        final Sprite roadTop = new Sprite32x32("roadtop", "quest.png", 0x60,
                MyColors.BROWN, MyColors.GRAY, MyColors.TAN, MyColors.GREEN);
        final Sprite roadRight = new Sprite32x32("roadRight", "quest.png", 0x61,
                MyColors.BROWN, MyColors.GRAY, MyColors.TAN, MyColors.GREEN);
        final Sprite roadCorner = new Sprite32x32("roadCorner", "quest.png", 0x62,
                MyColors.BROWN, MyColors.GRAY, MyColors.TAN, MyColors.GREEN);

        for (int i = 2; i < 7; ++i) {
            result.add(new QuestBackground(new Point(i,0), roadTop, true));
            result.add(new QuestBackground(new Point(7, i-1), roadRight, true));
        }
        result.add(new QuestBackground(new Point(7,0), roadCorner, true));

        return result;
    }
}
