package model.quests;

import model.Model;
import model.achievements.Achievement;
import model.classes.Skill;
import model.enemies.AutomatonEnemy;
import model.enemies.Enemy;
import model.enemies.LibraryAutomatonEnemy;
import model.journal.StoryPart;
import model.quests.scenes.ArrowlessEdge;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.QuestSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TroubleInTheLibraryQuest extends MainQuest {

    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackgroundSprites();

    private static MyColors BACKGROUND_COLOR = MyColors.BLACK;
    public static final String QUEST_NAME = "Trouble in the Library";
    private static final String INTRO_TEXT = "In search of invaluable information, the party has come to a large library. " +
            "Before anything can be learned however, you must first disable the magical automatons which have run amuck within.";
    private static final String ENDING_TEXT = "The automatons have been disabled, and the library can once again be visited by civilians.";

    private static final Sprite32x32 RED_PATH_SPRITE = new Sprite32x32("redpath", "quest.png", 0x4D,
            MyColors.RED, BACKGROUND_COLOR, MyColors.RED, MyColors.BROWN);
    private static final Sprite BLUE_PATH_SPRITE =  new Sprite32x32("greenpath", "quest.png", 0x4D,
            MyColors.LIGHT_BLUE, BACKGROUND_COLOR, MyColors.RED, MyColors.BROWN);
    private static final Sprite GOLD_PATH_SPRITE = new Sprite32x32("greenpath", "quest.png", 0x5D,
            BACKGROUND_COLOR, MyColors.GOLD, MyColors.RED, MyColors.BROWN);
    private static final Sprite BOTH_PATH_SPRITE = new Sprite32x32("greenpath", "quest.png", 0x1C,
            MyColors.LIGHT_BLUE, MyColors.GOLD, MyColors.RED, MyColors.BROWN);

    private QuestNode[][] nodeGrid;
    private List<MovingEnemyGroup> enemies;
    private List<List<Point>> enemyPaths;
    private ArrayList<QuestBackground> decorations;
    private int logicStep = 0;
    private boolean redShowing = false;
    private boolean blueShowing = false;
    private boolean goldShowing = false;

    public TroubleInTheLibraryQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD,
                new Reward(0), 0, INTRO_TEXT, ENDING_TEXT);
    }

    @Override
    public Achievement.Data getAchievementData() {
        return makeAchievement(this, "You helped Willis shut down her renegade automatons that were wrecking the library.");
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Discern Automaton Behavior", List.of(
                new SoloSkillCheckSubScene(4, 4, Skill.Logic, 8,
                        "Wait a minute. It feels like there's a pattern to these machines' behavior."))),
                new QuestScene("Check Dead Automatons", List.of(
                        new ConditionSubScene(7, 8) {
                            @Override
                            public String getDescription() {
                                return "Exit library.";
                            }

                            @Override
                            public QuestEdge run(Model model, QuestState state) {
                                for (MovingEnemyGroup group : enemies) {
                                    for (Enemy e : group.getEnemies()) {
                                        if (!e.isDead()) {
                                            state.println("There are still automatons roaming in the library!");
                                            return getFailEdge();
                                        }
                                    }
                                }
                                return getSuccessEdge();
                            }
                        }
                )),
                new QuestScene("Shut down machine", List.of(
                new SoloSkillCheckSubScene(4, 8, Skill.MagicAny, 10,
                        "Here's the arcane machine which is powering the automatons. How do we shut it down?"))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        makePathsAndDecorations();
        makeEnemies();

        List<QuestJunction> juncs = new ArrayList<>();
        nodeGrid = new QuestNode[8][9];
        QuestSubScene subScene = scenes.get(0).get(0);
        nodeGrid[subScene.getColumn()][subScene.getRow()] = subScene;

        nodeGrid[3][4] = new StoryJunction(3, 4, new QuestEdge(nodeGrid[3][3], QuestEdge.VERTICAL)) {
            @Override
            protected void doAction(Model model, QuestState state) {
                switch (logicStep) {
                    case 0:
                        showRedPath(model, state);
                        break;
                    case 1:
                        showBluePath(model, state);
                        break;
                    case 2:
                        showGoldPath(model, state);
                        break;
                    default:
                        state.leaderSay("They always move along those paths huh?");
                }
                logicStep++;
            }
        };
        nodeGrid[5][8] = new StoryJunction(5, 8, new QuestEdge(getSuccessEndingNode())) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.println("The magical machine shuts down, and so does all the automatons in the library. " +
                        "It is eerily quiet now.");
                for (int y = 0; y < nodeGrid[0].length; y++) {
                    for (int x = 0; x < nodeGrid.length; x++) {
                        if (nodeGrid[x][y] instanceof ChooseNode) {
                            ChooseNode node = (ChooseNode) nodeGrid[x][y];
                            if (node.hasEnemies()) {
                                node.clearEnemyGroups();
                                node.addPoof();
                            }
                        }
                    }
                }
            }
        };
        nodeGrid[0][0] = new QuestStartPointWithoutDecision(new QuestEdge(nodeGrid[1][0]), "In we go...");
        QuestSubScene conditionScene = scenes.get(1).get(0);
        nodeGrid[conditionScene.getColumn()][conditionScene.getRow()] = conditionScene;
        QuestSubScene machineScene = scenes.get(2).get(0);
        nodeGrid[machineScene.getColumn()][machineScene.getRow()] = machineScene;

        for (int y = 7; y >= 0; --y) {
            addChooseNode(nodeGrid, 7, y, List.of(new ArrowlessEdge(nodeGrid[7][y+1])));
        }

        for (int x = 6; x >= 0; --x) {
            addChooseNode(nodeGrid, x, 7, List.of(new ArrowlessEdge(nodeGrid[x+1][7])));
            for (int y = 6; y >= 0; --y) {
                if (nodeGrid[x][y] == null) {
                    List<QuestEdge> connections = new ArrayList<>();
                    if (nodeGrid[x][y + 1] instanceof ChooseNode) {
                        connections.add(new ArrowlessEdge(nodeGrid[x][y+1]));
                    }
                    if (nodeGrid[x+1][y] instanceof ChooseNode) {
                        connections.add(new ArrowlessEdge(nodeGrid[x+1][y]));
                    }
                    addChooseNode(nodeGrid, x, y, connections);
                }
            }
        }

        for (int y = 0; y < nodeGrid[0].length - 1; y++) {
            for (int x = 1; x < nodeGrid.length; ++x) {
                if (nodeGrid[x][y] instanceof QuestJunction && nodeGrid[x-1][y] instanceof ChooseNode) {
                    ((QuestJunction) nodeGrid[x][y]).connectTo(new ArrowlessEdge(nodeGrid[x - 1][y]));
                }
            }
        }

        for (int x = 0; x < nodeGrid.length; ++x) {
            for (int y = 1; y < nodeGrid[0].length - 1; ++y) {
                if (nodeGrid[x][y] instanceof QuestJunction && nodeGrid[x][y-1] instanceof ChooseNode) {
                    ((QuestJunction) nodeGrid[x][y]).connectTo(new ArrowlessEdge(nodeGrid[x][y - 1]));
                }
            }
        }

        ((QuestJunction)nodeGrid[subScene.getColumn()][subScene.getRow()-1]).connectTo(new QuestEdge(subScene));

        ((QuestJunction)nodeGrid[3][4]).getConnections().clear();
        ((QuestJunction)nodeGrid[3][4]).connectTo(new QuestEdge(nodeGrid[3][3], QuestEdge.VERTICAL));

        ((QuestJunction)nodeGrid[0][0]).getConnections().clear();
        ((QuestJunction)nodeGrid[0][0]).connectTo(new QuestEdge(nodeGrid[1][0], QuestEdge.HORIZONTAL));

        conditionScene.connectSuccess(getSuccessEndingNode());
        conditionScene.connectFail(nodeGrid[7][7]);

        ((QuestJunction)nodeGrid[machineScene.getColumn()][machineScene.getRow()-1]).connectTo(new QuestEdge(machineScene));

        ((QuestJunction)nodeGrid[0][7]).connectTo(new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL));

        for (int y = 0; y < nodeGrid[0].length; y++) {
            for (int x = 0; x < nodeGrid.length; x++) {
                if (nodeGrid[x][y] instanceof QuestJunction) {
                    juncs.add((QuestJunction) nodeGrid[x][y]);
                }
            }
        }

        for (MovingEnemyGroup enemy : enemies) {
            enemy.setOnPath(nodeGrid);
        }
        return juncs;
    }

    private void showGoldPath(Model model, QuestState state) {
        if (goldShowing) {
            return;
        }
        state.leaderSay("So you're telling me the gold automatons move according to the gold pattern?");
        goldShowing = true;
        for (Point p : enemyPaths.get(2)) {
            if (enemyPaths.get(1).contains(p) && blueShowing) {
                decorations.add(new QuestBackground(p, BOTH_PATH_SPRITE, false));
            } else {
                decorations.add(new QuestBackground(p, GOLD_PATH_SPRITE, false));
            }
        }
    }

    private void showBluePath(Model model, QuestState state) {
        if (blueShowing) {
            return;
        }
        state.leaderSay("So you're telling me the blue automatons move according to the blue pattern?");
        blueShowing = true;
        for (Point p : enemyPaths.get(1)) {
            if (enemyPaths.get(2).contains(p) && goldShowing) {
                decorations.add(new QuestBackground(p, BOTH_PATH_SPRITE, false));
            } else {
                decorations.add(new QuestBackground(p, BLUE_PATH_SPRITE, false));
            }
        }
    }

    private void showRedPath(Model model, QuestState state) {
        if (redShowing) {
            return;
        }
        redShowing = true;
        state.leaderSay("So you're telling me the red automatons move according to the red pattern?");
        for (Point p : enemyPaths.get(0)) {
            decorations.add(new QuestBackground(p, RED_PATH_SPRITE, false));
        }
    }

    private void makePathsAndDecorations() {
        enemyPaths = new ArrayList<>();
        enemyPaths.add(List.of(
                new Point(5, 0), new Point(5, 1), new Point(6, 1), new Point(7, 1),
                new Point(7, 2), new Point(6, 2), new Point(6, 1), new Point(6, 0)));
        enemyPaths.add(List.of(
                new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(1, 3),
                new Point(1, 4), new Point(1, 5), new Point(1, 6), new Point(1, 7),
                new Point(2, 7), new Point(3, 7), new Point(4, 7), new Point(4, 6),
                new Point(4, 5), new Point(5, 5), new Point(6, 5), new Point(7, 5),
                new Point(7, 6), new Point(7, 7), new Point(6, 7), new Point(5, 7),
                new Point(5, 6), new Point(5, 5), new Point(5, 4), new Point(5, 3),
                new Point(5, 2), new Point(4, 2), new Point(3, 2), new Point(2, 2),
                new Point(1, 2), new Point(0, 2)));
        enemyPaths.add(List.of(
                new Point(3, 1), new Point(4, 1), new Point(4, 0), new Point(3, 0),
                new Point(3, 1), new Point(3, 2), new Point(3, 3), new Point(4, 3),
                new Point(5, 3), new Point(6, 3), new Point(7, 3), new Point(7, 4),
                new Point(6, 4), new Point(6, 5), new Point(6, 6), new Point(5, 6),
                new Point(4, 6), new Point(3, 6), new Point(3, 5), new Point(2, 5),
                new Point(2, 6), new Point(1, 6), new Point(0, 6), new Point(0, 5),
                new Point(0, 4), new Point(1, 4), new Point(2, 4), new Point(2, 3),
                new Point(2, 2), new Point(2, 1)));

        this.decorations = new ArrayList<>();
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        decorations = new ArrayList<>();
        redShowing = false;
        blueShowing = false;
        goldShowing = false;
    }

    private void makeEnemies() {
        enemies = new ArrayList<>();
        List<MyColors> colors = List.of(MyColors.RED, MyColors.LIGHT_BLUE, MyColors.GOLD);
        for (int j = 0; j < colors.size(); ++j) {
            if (j == 0) {
                enemies.add(new MovingEnemyGroup("Automatons", makeAutomatons(6, colors.get(j)), enemyPaths.get(j)));
            } else {
                enemies.add(new MovingEnemyGroup("Automatons", makeAutomatons(2, colors.get(j)), enemyPaths.get(j), j));
                enemies.add(new MovingEnemyGroup("Automatons", makeAutomatons(3, colors.get(j)), enemyPaths.get(j), j+7));
                enemies.add(new MovingEnemyGroup("Automatons", makeAutomatons(3, colors.get(j)), enemyPaths.get(j), j+15));
                enemies.add(new MovingEnemyGroup("Automatons", makeAutomatons(2, colors.get(j)), enemyPaths.get(j), j+23));
            }
        }
    }

    private List<Enemy> makeAutomatons(int num, MyColors color) {
        List<Enemy> automatons = new ArrayList<>();
        for (int i = 0; i < num; ++i) {
            automatons.add(new LibraryAutomatonEnemy('A', color));
        }
        return automatons;

    }

    private void addChooseNode(QuestNode[][] nodeGrid, int x, int y, List<QuestEdge> questEdges) {
        nodeGrid[x][y] = new ChooseNode(x, y, questEdges) {
            @Override
            protected boolean preRunHook(Model model, QuestState state) {
                if (hasEnemies()) {
                    if (runCombat(model, state)) {
                        return true;
                    }
                }
                moveAllAutomatons(model, state);
                if (hasEnemies()) {
                    if (runCombat(model, state)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private void moveAllAutomatons(Model model, QuestState state) {
        state.setCursorEnabled(false);
        for (MovingEnemyGroup group : enemies) {
            ChooseNode node = group.getNode();
            QuestEdge edge = group.getEdgeToMoveTo(model, state, nodeGrid);
            if (edge != null && edge.getNode() instanceof ChooseNode) {
                ChooseNode destination = ((ChooseNode) edge.getNode());
                node.removeEnemyGroup(group);
                QuestSubView.animateAvatarAlongEdge(state, node.getPosition(), edge, group.getSprite());
                destination.addEnemyGroup(group);
                group.setNode(destination);
            }
        }
        state.setCursorEnabled(true);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        for (QuestJunction junc : junctions) {
            if (junc.getRow() == 4) {
                if (junc.getColumn() == 5) {
                    scenes.get(0).get(0).connectFail(junc);
                } else if (junc.getColumn() == 3) {
                    scenes.get(0).get(0).connectSuccess(junc);
                }
            } else if (junc.getRow() == 0) {
                if (junc.getColumn() == 2) {
                    scenes.get(2).get(0).connectFail(junc);
                }
            } else if (junc.getRow() == 8) {
                if (junc.getColumn() == 5) {
                    scenes.get(2).get(0).connectSuccess(junc);
                }
            }
        }
    }

    @Override
    public MyColors getBackgroundColor() {
        return BACKGROUND_COLOR;
    }

    @Override
    public List<QuestBackground> getDecorations() {
        if (decorations == null) {
            return super.getDecorations();
        }
        return decorations;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.darkQuestSong;
    }

    @Override
    public MainQuest copy() {
        return new TroubleInTheLibraryQuest();
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> backgrounds = new ArrayList<>();
        Sprite32x32 entrance = new Sprite32x32("entrance", "quest.png", 0x41,
                MyColors.DARK_GRAY, MyColors.TAN, MyColors.DARK_BROWN, MyColors.CYAN);
        backgrounds.add(new QuestBackground(new Point(0, 0), entrance));
        Sprite32x32 bookCase1 = new Sprite32x32("entrance", "quest.png", 0x3E,
                MyColors.DARK_GRAY, MyColors.TAN, MyColors.DARK_BROWN, MyColors.BROWN);
        Sprite32x32 bookCase2 = new Sprite32x32("entrance", "quest.png", 0x4E,
                MyColors.DARK_GRAY, MyColors.TAN, MyColors.DARK_BROWN, MyColors.GRAY_RED);
        Sprite32x32 bookCase3 = new Sprite32x32("entrance", "quest.png", 0x4F,
                MyColors.DARK_GRAY, MyColors.TAN, MyColors.DARK_BROWN, MyColors.GRAY_RED);
        backgrounds.add(new QuestBackground(new Point(1, 0), bookCase1));
        backgrounds.add(new QuestBackground(new Point(2, 0), bookCase2));
        backgrounds.add(new QuestBackground(new Point(3, 0), bookCase3));
        backgrounds.add(new QuestBackground(new Point(4, 0), bookCase1));
        backgrounds.add(new QuestBackground(new Point(5, 0), bookCase2));
        backgrounds.add(new QuestBackground(new Point(6, 0), bookCase3));
        backgrounds.add(new QuestBackground(new Point(7, 0), bookCase1));

        Sprite32x32 floor = new Sprite32x32("libraryfloor", "dungeon.png", 0x00,
                MyColors.DARK_GRAY, MyColors.CYAN, MyColors.PINK, MyColors.DARK_BROWN);
        for (int y = 1; y < 9; ++y) {
            for (int x = 0; x < 8; ++x) {
                if (y == 5 && (x == 3 || x == 4)) {
                    backgrounds.add(new QuestBackground(new Point(x, y), bookCase2));
                } else if (y == 8 && (x == 1 || x == 3 || x == 5 || x == 6)) {
                    backgrounds.add(new QuestBackground(new Point(x, y), bookCase1));
                } else {
                    backgrounds.add(new QuestBackground(new Point(x, y), floor));
                }
            }
        }
        return backgrounds;
    }
}
