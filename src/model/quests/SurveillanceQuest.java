package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.OrcWarrior;
import model.quests.scenes.*;
import model.states.QuestState;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SurveillanceQuest extends Quest {
    private static final String START_TEXT =
            "A general has approached you and given you a mission; reconnoiter and survey an Orcish " +
                    "military camp. You must gather as much intel as possible, without getting caught.";
    private static final String END_TEXT =
            "You return to the general with your report. The general seems pleased and compensates you for " +
            "your service.";
    private static List<QuestBackground> bgSprites = makeBackgroundSprites();
    private GameCharacter wallClimber;

    public SurveillanceQuest() {
        super("Surveillance", "a general", QuestDifficulty.MEDIUM, 1, 35, START_TEXT, END_TEXT);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Travel",
                        List.of(new CollectiveSkillCheckSubScene(0, 1, Skill.Survival, 3,
                                        "It's quite the hike to get to the camp."),
                                new PayGoldSubScene(3, 0, 8,
                                        "We can hire some horses, but it will be expensive."))),
                new QuestScene("Survey",
                        List.of(new CollectiveSkillCheckSubScene(3, 1, Skill.Sneak, 2,
                                "Let's take care not to be spotted from the camp."),
                                new CollaborativeSkillCheckSubScene(1, 3, Skill.Perception, 7,
                                        "Okay gang, fan out. Let's see what we can see."))),
                new QuestScene("Infiltrate",
                        List.of(new ClimbOverWallSubScene(6, 4),
                                new SneakAroundTheCampSubScene(7, 5),
                                new CollaborativeSkillCheckSubScene(5, 4, Skill.Persuade, 10,
                                        "Or perhaps we can persuade them to let us in and have a look around?"))),
                new QuestScene("Alarm Raised",
                        List.of(new OrcsCombatSubScene(2, 6, 3),
                                new SoloSkillCheckSubScene(3, 6, Skill.Leadership, 7,
                                        "Let's get out of here before the whole garrison comes to fight us."),
                                new OrcsCombatSubScene(3, 7, 6))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint start = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(0).get(1))),
                "We're setting out for the military camp.");

        QuestDecisionPoint qd1 = new QuestDecisionPoint(5, 2,
                List.of(new QuestEdge(scenes.get(2).get(0)),
                        new QuestEdge(scenes.get(2).get(2))),
                "We're going to need to find out more about what's in the camp. But how?");

        SimpleJunction sj = new SimpleJunction(4, 5, new QuestEdge(scenes.get(3).get(0)));
        SimpleJunction sj2 = new SimpleJunction(5, 6, new QuestEdge(getSuccessEndingNode()));
        return List.of(start, qd1, sj, sj2);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(scenes.get(1).get(0));
        scenes.get(0).get(1).connectSuccess(scenes.get(1).get(0), QuestEdge.VERTICAL);

        scenes.get(1).get(0).connectFail(junctions.get(2));
        scenes.get(1).get(0).connectSuccess(scenes.get(1).get(1), QuestEdge.VERTICAL);
        scenes.get(1).get(1).connectFail(getFailEndingNode());
        scenes.get(1).get(1).connectSuccess(junctions.get(1), QuestEdge.VERTICAL);

        scenes.get(2).get(1).connectFail(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(2).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(2).get(0).connectFail(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(2).get(0).connectSuccess(scenes.get(2).get(1));
        scenes.get(2).get(2).connectFail(junctions.get(2));
        scenes.get(2).get(2).connectSuccess(junctions.get(3), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectSuccess(scenes.get(3).get(1), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectFail(scenes.get(3).get(2), QuestEdge.VERTICAL);
        scenes.get(3).get(1).connectSuccess(junctions.get(3));
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
        final Sprite woods = new Sprite32x32("woodsqmb", "quest.png", 0x53,
                MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.GREEN);
        for (int row = 0; row < 4; row++) {
            for (int col = 4; col < 8; col++) {
                if (col - row > 3) {
                    result.add(new QuestBackground(new Point(col, row), woods, true));
                }
            }
        }

        final Sprite camp = new Sprite32x32("campqmb", "quest.png", 0x56,
                MyColors.BLACK, MyColors.BROWN, MyColors.TAN, MyColors.GREEN);
        result.add(new QuestBackground(new Point(2,5), camp, true));
        final Sprite horseCart = new Sprite32x32("horsecart", "quest.png", 0x57,
                MyColors.BROWN, MyColors.GRAY, MyColors.TAN, MyColors.GREEN);
        result.add(new QuestBackground(new Point(1,0), horseCart, true));

        final Sprite roadTop = new Sprite32x32("roadtop", "quest.png", 0x60,
                MyColors.BROWN, MyColors.GRAY, MyColors.TAN, MyColors.GREEN);
        final Sprite roadRight = new Sprite32x32("roadRight", "quest.png", 0x61,
                MyColors.BROWN, MyColors.GRAY, MyColors.TAN, MyColors.GREEN);
        final Sprite roadCorner = new Sprite32x32("roadCorner", "quest.png", 0x62,
                MyColors.BROWN, MyColors.GRAY, MyColors.TAN, MyColors.GREEN);

        for (int i = 2; i < 6; ++i) {
            if (i < 4) {
                result.add(new QuestBackground(new Point(i, 0), roadTop, true));
            }
            result.add(new QuestBackground(new Point(3, i - 1), roadRight, true));
        }
        result.add(new QuestBackground(new Point(3,0), roadCorner, true));

        return result;
    }

    private static class OrcsCombatSubScene extends CombatSubScene {
        private String text;

        public OrcsCombatSubScene(int col, int row, int num) {
            super(col, row, generateEnemies(num), false);
            this.text = "orcs";
            if (num > 3) {
                this.text = "lots of orcs";
            }
        }

        private static List<Enemy> generateEnemies(int num) {
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < num; ++i) {
                enemies.add(new OrcWarrior('A'));
            }
            return enemies;
        }

        @Override
        protected String getCombatDetails() {
            return text;
        }
    }

    private class ClimbOverWallSubScene extends SoloSkillCheckSubScene {
        public ClimbOverWallSubScene(int col, int row) {
            super(col, row,  Skill.Acrobatics, 7,
                    "Maybe one of us can climb over the wall and sneak around inside.");
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge toReturn = super.run(model, state);
            wallClimber = getPerformer();
            return toReturn;
        }
    }

    private class SneakAroundTheCampSubScene extends SkillQuestSubScene {
        public SneakAroundTheCampSubScene(int col, int row) {
            super(col, row);
            //, Skill.Sneak, 10, "Just don't get caught!"
        }


        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(SoloSkillCheckSubScene.SPRITE.getName(), new Point(xPos, yPos),
                    SoloSkillCheckSubScene.SPRITE, 1);
        }

        @Override
        public String getDescription() {
            return "Skill Check Sneak 10 (same character as Acrobatics check)";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.setCursorEnabled(false);
            if (model.getParty().size() > 1) {
                leaderSay(model, "Just don't get caught!");
            }
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, state,
                    wallClimber, Skill.Sneak, 10, 20, 0);
            state.setCursorEnabled(true);
            if (result.isSuccessful()) {
                return getSuccessEdge();
            }
            return getFailEdge();
        }
    }
}
