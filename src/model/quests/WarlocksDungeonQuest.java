package model.quests;

import model.Model;
import model.SteppingMatrix;
import model.classes.Skill;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.TrapSubScene;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class WarlocksDungeonQuest extends Quest {
    private static final String INTRO =
            "Caught in an evil warlock's vast dungeon, you must use all your wits " +
            "to escape a labyrinth of dark halls, dangerous chambers and confusing clues.";
    private static final String OUTRO =
            "You finally escape the dungeon and vow to one day return and deliver " +
            "vengeance upon the evil warlock.";
    private boolean flipped = false;

    public WarlocksDungeonQuest() {
        super("Warlock's Dungeon", "Warlock", QuestDifficulty.EASY, 1, 0, 100, INTRO, OUTRO);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Just TURN Left", List.of(
                    new CollaborativeSkillCheckSubScene(3, 0, Skill.Perception, 10, ""),
                    new CollaborativeSkillCheckSubScene(3, 1, Skill.Perception, 10, ""),
                    new CollaborativeSkillCheckSubScene(3, 2, Skill.Perception, 10, ""))),
                new QuestScene("And IT Will", List.of(
                    new TrapSubScene(2, 3, 7),
                    new TrapSubScene(4, 5, 7),
                    new TrapSubScene(1, 5, 7))),
                new QuestScene("Be OVER Sooon", List.of(
                    new CollaborativeSkillCheckSubScene(3, 5, Skill.Logic, 11, ""),
                    new CollaborativeSkillCheckSubScene(4, 7, Skill.Logic, 11, ""),
                    new CollaborativeSkillCheckSubScene(5, 8, Skill.Logic, 11, "")
                )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint start = new QuestDecisionPoint(2, 1, List.of(
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(0).get(1), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(0).get(2), QuestEdge.VERTICAL)),
                "There's got to be some kind of way out of here...");

        SimpleJunction tmp4 = new SimpleJunction(1, 2, new QuestEdge(start, QuestEdge.VERTICAL, MyColors.WHITE));

        SimpleJunction wrapperLeft7 = new WrapperOutNode(0, 2, new QuestEdge(tmp4), false, MyColors.WHITE);
        QuestJunction wrapperRight7 = new WrapperInNode(7, 2, wrapperLeft7, true, MyColors.WHITE);

        SimpleJunction tmp3 = new SimpleJunction(6, 2, new QuestEdge(wrapperRight7));
        SimpleJunction wrapperRight6 = new WrapperOutNode(7, 4, new QuestEdge(tmp3), true, MyColors.WHITE);
        QuestJunction wrapLeft6 = new WrapperInNode(0, 5, wrapperRight6, false, MyColors.WHITE);

        SimpleJunction wrapperRight5 = new WrapperOutNode(7, 6, new QuestEdge(scenes.get(2).get(1), QuestEdge.HORIZONTAL, MyColors.LIGHT_RED), true, MyColors.LIGHT_RED);
        QuestJunction wrapperLeft5 = new WrapperInNode(0, 7, wrapperRight5, false, MyColors.LIGHT_GREEN);

        SimpleJunction wrapperLeft4 = new WrapperOutNode(0, 6, new QuestEdge(wrapperLeft5, QuestEdge.VERTICAL, MyColors.LIGHT_GREEN), false, MyColors.LIGHT_GREEN);
        QuestJunction wrapperRight4 = new WrapperInNode(7, 5, wrapperLeft4, true, MyColors.WHITE);

        SimpleJunction wrapperRight3 = new WrapperOutNode(7, 3, new QuestEdge(scenes.get(1).get(1), QuestEdge.HORIZONTAL, MyColors.LIGHT_GREEN), true, MyColors.LIGHT_GREEN);
        QuestJunction wrapperLeft3 = new WrapperInNode(0, 4, wrapperRight3, false, MyColors.LIGHT_GREEN);

        SimpleJunction wrapperLeft1 = new WrapperOutNode(0, 0, new QuestEdge(wrapperLeft3, QuestEdge.VERTICAL, MyColors.LIGHT_GREEN), false, MyColors.LIGHT_GREEN);
        QuestJunction wrapperRight1 = new WrapperInNode(7, 0, wrapperLeft1, true, MyColors.LIGHT_GREEN);

        SimpleJunction wrapperLeft2 = new WrapperOutNode(0, 1, new QuestEdge(start, QuestEdge.VERTICAL, MyColors.LIGHT_RED), false, MyColors.LIGHT_RED);
        QuestJunction wrapperRight2 = new WrapperInNode(7, 1, wrapperLeft2, true, MyColors.LIGHT_RED);

        SimpleJunction tmp1 = new SimpleJunction(5, 1, new QuestEdge(wrapperRight2, QuestEdge.HORIZONTAL, MyColors.LIGHT_RED));
        SimpleJunction tmp2 = new SimpleJunction(5, 7, new QuestEdge(scenes.get(2).get(2), QuestEdge.VERTICAL));

        SimpleJunction tmp5 = new SimpleJunction(5, 4, new QuestEdge(tmp2));
        tmp5.getConnections().add(new QuestEdge(wrapperLeft3));

        QuestJunction wrapperRight8 = new WrapperOutNode(7, 7, new QuestEdge(tmp2), true, MyColors.WHITE);
        QuestJunction wrapperLeft8 = new WrapperOutNode(0, 3, new QuestEdge(getFailEndingNode(), QuestEdge.HORIZONTAL, MyColors.LIGHT_RED), false, MyColors.LIGHT_RED);

        QuestDecisionPoint qd2 = new QuestDecisionPoint(2, 6,
                List.of(new QuestEdge(scenes.get(2).get(0)), new QuestEdge(scenes.get(1).get(0))), "Hmmm...");

        return List.of(start, wrapperRight2, wrapperLeft2, wrapperRight1, wrapperLeft1, wrapperRight3, wrapperLeft3, tmp1,
                wrapperRight4, wrapperLeft4, wrapperRight5, wrapperLeft5, tmp2, wrapperRight6, wrapLeft6, tmp3, wrapperRight7,
                wrapperLeft7, tmp4, tmp5, wrapperRight8, wrapperLeft8, qd2);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(junctions.get(7));
        scenes.get(0).get(0).connectSuccess(junctions.get(3));

        scenes.get(0).get(1).connectFail(junctions.get(7));
        scenes.get(0).get(1).connectSuccess(scenes.get(0).get(0));

        scenes.get(0).get(2).connectFail(scenes.get(0).get(1));
        scenes.get(0).get(2).connectSuccess(junctions.get(7));

        scenes.get(1).get(0).connectSuccess(junctions.get(18));
        scenes.get(1).get(1).connectSuccess(junctions.get(8));
        scenes.get(1).get(2).connectSuccess(junctions.get(14));

        scenes.get(2).get(0).connectFail(scenes.get(0).get(2));
        scenes.get(2).get(0).connectSuccess(scenes.get(1).get(2));

        scenes.get(2).get(1).connectFail(junctions.get(22));
        scenes.get(2).get(1).connectSuccess(junctions.get(12));

        scenes.get(2).get(2).connectFail(junctions.get(22));
        scenes.get(2).get(2).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        // TODO: Make a fake white double-arrow from (0, 4) to (5, 7)
        // TODO: Make a fake wrapper arrow from (7, 7) to (5, 7)
        return super.getBackgroundSprites();
    }

    private static final Sprite32x32[] WRAP_RIGHT = new Sprite32x32[]{
            new Sprite32x32("wraprightwhite", "quest.png", 0x16, MyColors.BLACK, MyColors.WHITE, MyColors.WHITE),
            new Sprite32x32("wraprightred", "quest.png", 0x16, MyColors.BLACK, MyColors.WHITE, MyColors.LIGHT_RED),
            new Sprite32x32("wraprightgreen", "quest.png", 0x16, MyColors.BLACK, MyColors.WHITE, MyColors.LIGHT_GREEN)};

    private static final Sprite32x32[] WRAP_LEFT = new Sprite32x32[]{
            new Sprite32x32("wrapleftwhite", "quest.png", 0x17, MyColors.BLACK, MyColors.WHITE, MyColors.WHITE),
            new Sprite32x32("wrapleftred", "quest.png", 0x17, MyColors.BLACK, MyColors.WHITE, MyColors.LIGHT_RED),
            new Sprite32x32("wrapleftgreen", "quest.png", 0x17, MyColors.BLACK, MyColors.WHITE, MyColors.LIGHT_GREEN)
    };

    private class WrapperInNode extends QuestJunction {
        private final QuestJunction connection;
        private Sprite sprite;

        public WrapperInNode(int col, int row, QuestJunction connection, boolean right, MyColors colorToUse) {
            super(col, row);
            this.connection = connection;
            Sprite32x32[] table = WRAP_LEFT;
            if (right) {
                table = WRAP_RIGHT;
            }
            if (colorToUse == MyColors.WHITE) {
                sprite = table[0];
            } else if (colorToUse == MyColors.LIGHT_RED) {
                sprite = table[1];
            } else {
                sprite = table[2];
            }
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(sprite.getName(), new Point(xPos, yPos), sprite, 1);
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.getSubView().setEdgesEnabled(false);
            state.getSubView().setSubScenesEnabled(false);
            state.getSubView().animateMovement(model, new Point(state.getCurrentPosition().getColumn(), state.getCurrentPosition().getRow()),
                    new QuestEdge(connection));
            state.setCurrentPosition(connection);
            state.getSubView().setEdgesEnabled(true);
            state.getSubView().setSubScenesEnabled(true);
            return connection.getConnection(0);
        }
    }

    private class WrapperOutNode extends SimpleJunction {
        private final boolean right;
        private Sprite sprite;

        public WrapperOutNode(int col, int row, QuestEdge questEdge, boolean right, MyColors colorToUse) {
            super(col, row, questEdge);
            this.right = right;
            Sprite32x32[] table = WRAP_LEFT;
            if (right) {
                table = WRAP_RIGHT;
            }
            if (colorToUse == MyColors.WHITE) {
                sprite = table[0];
            } else if (colorToUse == MyColors.LIGHT_RED) {
                sprite = table[1];
            } else {
                sprite = table[2];
            }
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            model.getScreenHandler().register(sprite.getName(), new Point(xPos, yPos), sprite, 1);
        }
    }
}
